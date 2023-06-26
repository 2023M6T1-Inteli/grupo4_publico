/**
 * A classe {@code ORToolsSolver} é responsável por realizar a otimização linear utilizando o método Simplex.
 * Foi projetada para resolver o problema do corte de bobinas dadas as larguras, quantidades e padrões iniciais de corte.
 *
 * @author Elias Biondo
 * @version 1.0
 * @since 2023-06-06
 */

// Definindo o pacote.
package inteli.cc6.algorithm;

// Importando bibliotecas necessárias.
import java.util.Arrays;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;
import inteli.cc6.models.CalcModel;
import inteli.cc6.models.CuttingPatternModel;
import inteli.cc6.repositories.CuttingPatternRepository;
import jakarta.transaction.Transactional;

// Definindo o solver.
public class ORToolsSolver {
    /** Definindo constantes úteis à classe */
    private static final double EPS = 1e-6;
    private static final double infinity = java.lang.Double.POSITIVE_INFINITY;

    /** Carrecando as bibliotecas nativas */
    static {
        Loader.loadNativeLibraries();
    }

    /** Definindo características gerais da classe */
    private final int[] coilWidths;                 // Largura das bobinas.
    private final int[] coilAmounts;                // Quantidade das bobinas.
    private final int machineMinWidth;              // Largura mínima da máquina.
    private final int machineMaxWidth;              // Largura máxima da máquina.
    private final int maxCoilsPerDraw;              // Número máximo de bobinas por tirada.
    private final int quantityOfItems;              // Número de itens.
    private int[] patternUsage;                     // Quantidade usada de cada padrão de corte.

    /** Definindo características gerais da resolução do problema principal */
    private int[][] patterns;
    private boolean isSolved;
    private MPSolver solver;
    private MPVariable[] variables;
    private MPVariable[] integerVariables;
    private MPConstraint constraint;
    private MPObjective objective;
    private double[] dualValues;

    /** Definindo características gerais da resolução do subproblema */
    private MPSolver knapsackSolver;
    private MPVariable[] knapsackVariables;
    private MPConstraint knapsackSizeConstraint;
    private MPConstraint knapsackMaxRollsConstraint;
    private MPObjective knapsackObjective;

    /** Definindo atributos de permancência */
    private CuttingPatternRepository cuttingPatternRepository;
    private CalcModel calc;

    /**
     * Construtor para a classe {@code ORToolsSolver}.
     *
     * @param coilWidths                     as larguras das bobinas.
     * @param coilAmounts                    as quantidades de bobinas.
     * @param machineMinWidth                a largura mínima suportada pela máquina.
     * @param machineMaxWidth                a largura máxima suportada pela máquina.
     * @param maxCoilsPerDraw                o número máximo de bobinas por padrão de corte suportado pela máquina.
     */
    public ORToolsSolver(
            int[] coilWidths,
            int[] coilAmounts,
            int machineMinWidth,
            int machineMaxWidth,
            int maxCoilsPerDraw,
            CuttingPatternRepository cuttingPatternRepository,
            CalcModel calc) {

        this.coilWidths = coilWidths;
        this.coilAmounts = coilAmounts;
        this.machineMinWidth = machineMinWidth;
        this.machineMaxWidth = machineMaxWidth;
        this.maxCoilsPerDraw = maxCoilsPerDraw;
        this.quantityOfItems = coilWidths.length;
        this.isSolved = false;
        this.cuttingPatternRepository = cuttingPatternRepository;
        this.calc = calc;

    }

    /**
     * Método para gerar padrões iniciais.
     * Inicializando a matriz de padrões e preenchendo a diagonal principal com o mínimo entre a largura máxima da máquina dividida pela largura das bobinas e o número máximo de bobinas por desenho.
     */
    public void generateInitialPatterns() {
        // Inicializando a matriz de padrões com a quantidade de itens
        this.patterns = new int[quantityOfItems][quantityOfItems];

        // Looping através dos itens
        for (int i = 0; i < quantityOfItems; i++) {
            // Preenchendo a diagonal principal da matriz de padrões
            patterns[i][i] = Math.min(machineMaxWidth / coilWidths[i], maxCoilsPerDraw);
        }
    }

    /**
     * Método para definir o solver.
     * Cria um solver usando a biblioteca GLOP.
     */
    public void setSolver() {
        // Criando o solver
        this.solver = MPSolver.createSolver("GLOP");
    }

    /**
     * Método para definir as variáveis.
     * Inicializando o array de variáveis e preenchendo cada elemento com um número variável do solver com um limite inferior de 0.0 e limite superior de infinito.
     */
    public void setVariables() {
        // Inicializando o array de variáveis com o comprimento dos padrões
        this.variables = new MPVariable[patterns.length];

        // Looping através dos padrões
        for (int j = 0; j < patterns.length; j++) {
            // Preenchendo o array de variáveis com um número variável do solver
            this.variables[j] = solver.makeNumVar(0.0, infinity, "x" + j);
        }
    }


    /**
     * Método para definir as restrições.
     * Cria uma restrição para cada item, definindo um limite inferior igual à quantidade de bobinas e um limite superior igual ao infinito.
     * Em seguida, define o coeficiente da restrição para cada variável, de acordo com os padrões.
     */
    public void setConstraints() {
        // Looping através dos itens
        for (int i = 0; i < quantityOfItems; i++) {
            // Criando uma restrição para cada item
            this.constraint = solver.makeConstraint(coilAmounts[i], infinity, "demand" + i);

            // Looping através dos padrões
            for (int j = 0; j < patterns.length; j++) {
                // Definindo o coeficiente da restrição para cada variável
                constraint.setCoefficient(this.variables[j], patterns[j][i]);
            }
        }
    }

    /**
     * Método para definir o objetivo.
     * Define o objetivo do solver e configura o coeficiente de cada variável para 1.
     * Em seguida, configura o solver para minimizar o objetivo.
     */
    public void setObjective() {
        // Definindo o objetivo do solver
        objective = solver.objective();

        // Looping através das variáveis
        for (MPVariable xj : this.variables) {
            // Configurando o coeficiente de cada variável para 1
            objective.setCoefficient(xj, 1);
        }

        // Configurando o solver para minimizar o objetivo
        objective.setMinimization();
    }

    /**
     * Método para resolver o problema.
     * Resolve o problema usando o solver e retorna o valor do objetivo.
     *
     * @return O valor do objetivo após a resolução do problema.
     */
    public double solveTrivial() {
        // Resolvendo o problema
        solver.solve();

        // Retornando o valor do objetivo
        return solver.objective().value();
    }


    /**
     * Método para resolver o problema da mochila (Knapsack).
     * Inicializa o array de valores duais com o valor dual de cada restrição.
     * Cria um novo solver e define as variáveis, o objetivo e as restrições da mochila.
     * Por fim, resolve o problema da mochila.
     */
    public void solveKnapsack() {
        // Inicializando o array de valores duais
        dualValues = new double[quantityOfItems];

        // Looping através dos itens
        for (int i = 0; i < quantityOfItems; i++) {
            // Definindo o valor dual para cada item
            dualValues[i] = solver.constraints()[i].dualValue();
        }

        // Criando um novo solver para o problema da mochila
        knapsackSolver = MPSolver.createSolver("SCIP");

        // Inicializando as variáveis da mochila
        knapsackVariables = new MPVariable[quantityOfItems];

        // Looping através dos itens
        for (int i = 0; i < quantityOfItems; i++) {
            // Definindo as variáveis da mochila
            knapsackVariables[i] = knapsackSolver.makeIntVar(0.0, (double) machineMaxWidth / coilWidths[i], "max_rolls"); // o limite superior garante a restrição de largura
        }

        // Definindo o objetivo da mochila
        knapsackObjective = knapsackSolver.objective();

        // Looping através dos itens
        for (int i = 0; i < quantityOfItems; i++) {
            // Definindo o coeficiente do objetivo para cada variável da mochila
            knapsackObjective.setCoefficient(knapsackVariables[i], dualValues[i]);
        }

        // Configurando o solver da mochila para maximizar o objetivo
        knapsackObjective.setMaximization();

        // Criando a restrição de tamanho da mochila
        knapsackSizeConstraint = knapsackSolver.makeConstraint(machineMinWidth, machineMaxWidth, "width");

        // Looping através dos itens
        for (int i = 0; i < quantityOfItems; i++) {
            // Definindo o coeficiente da restrição de tamanho para cada variável da mochila
            knapsackSizeConstraint.setCoefficient(knapsackVariables[i], coilWidths[i]);
        }

        // Criando a restrição de número máximo de bobinas da mochila
        knapsackMaxRollsConstraint = knapsackSolver.makeConstraint(0.0, maxCoilsPerDraw, "max_rolls");

        // Looping através dos itens
        for (int i = 0; i < quantityOfItems; i++) {
            // Definindo o coeficiente da restrição de número máximo de bobinas para cada variável da mochila
            knapsackMaxRollsConstraint.setCoefficient(knapsackVariables[i], 1);
        }

        // Resolvendo o problema da mochila
        knapsackSolver.solve();
    }

    /**
     * Método para verificar se um valor objetivo é ótimo.
     *
     * @param objectiveValue O valor objetivo a ser verificado.
     * @return Verdadeiro se o valor objetivo é menor do que 1 + EPS, falso caso contrário.
     */
    public boolean isOptimal(double objectiveValue) {
        // Retornando verdadeiro se o valor objetivo é ótimo, falso caso contrário
        return objectiveValue < 1 + EPS;
    }

    /**
     * Método para inicializar e resolver o problema de otimização inteira.
     * Cria um novo solver, define as variáveis inteiras, restrições e objetivo, e resolve o problema.
     * Por fim, preenche o array de uso de padrões com a solução obtida e marca o problema como resolvido.
     */
    @Transactional
    public void intSolver() {
        // Criando um novo solver para o problema de otimização inteira
        MPSolver intSolver = MPSolver.createSolver("SCIP");

        // Inicializando as variáveis inteiras
        integerVariables = new MPVariable[patterns.length];

        // Looping através dos padrões
        for (int j = 0; j < patterns.length; j++) {
            // Definindo as variáveis inteiras
            integerVariables[j] = intSolver.makeIntVar(0.0, infinity, "x" + j);
        }

        // Looping através dos itens
        for (int i = 0; i < quantityOfItems; i++) {
            // Criando uma restrição para cada item
            MPConstraint constraint = intSolver.makeConstraint(coilAmounts[i], infinity, "demand" + i);

            // Looping através dos padrões
            for (int j = 0; j < patterns.length; j++) {
                // Definindo o coeficiente da restrição para cada variável inteira
                constraint.setCoefficient(integerVariables[j], patterns[j][i]);
            }
        }

        // Definindo o objetivo do problema de otimização inteira
        MPObjective intObjective = intSolver.objective();

        // Looping através dos padrões
        for (int j = 0; j < patterns.length; j++) {
            // Definindo o coeficiente do objetivo para cada variável inteira
            intObjective.setCoefficient(integerVariables[j], 1);
        }

        // Configurando o solver para minimizar o objetivo
        intObjective.setMinimization();

        // Resolvendo o problema de otimização inteira
        intSolver.solve();

        // Inicializando o array de uso de padrões
        patternUsage = new int[patterns.length];

        // Looping através dos padrões
        for (int j = 0; j < patterns.length; ++j) {
            patternUsage[j] = (int) integerVariables[j].solutionValue();
            if (patternUsage[j] > 0) {
                CuttingPatternModel cuttingPattern = new CuttingPatternModel();
                cuttingPattern.setDrawn(patternUsage[j]);
                cuttingPattern.setPattern(Arrays.toString(patterns[j]));
                cuttingPattern.setCalc(calc);
                cuttingPatternRepository.save(cuttingPattern);
            }
        }

        // Marcando o problema como resolvido
        isSolved = true;
    }

    private int calculateWaste(int[] pattern) {
        int totalWidth = 0;
        for (int i = 0; i < pattern.length; i++) {
            totalWidth += pattern[i] * coilWidths[i];
        }
        return machineMaxWidth - totalWidth;
    }


    /**
     * Método para resolver o problema.
     * Gera os padrões iniciais e entra em um loop infinito onde configura o solver, as variáveis, as restrições e o objetivo, e então resolve o problema trivial.
     * Depois disso, resolve o problema da mochila e verifica se o valor objetivo é ótimo.
     * Se for ótimo, chama o método intSolver e quebra o loop.
     * Se não for ótimo, gera um novo padrão a partir da solução do problema da mochila e adiciona ao array de padrões.
     */
    public CalcModel solve() {
        // Gerando os padrões iniciais
        generateInitialPatterns();

        // Entrando em um loop infinito
        while(true) {
            // Configurando o solver
            setSolver();

            // Configurando as variáveis
            setVariables();

            // Configurando as restrições
            setConstraints();

            // Configurando o objetivo
            setObjective();

            // Resolvendo o problema trivial
            solveTrivial();

            // Resolvendo o problema da mochila
            solveKnapsack();

            // Verificando se o valor objetivo é ótimo
            if (isOptimal(knapsackObjective.value())) {
                // Chamando o método intSolver
                intSolver();

                // Quebrando o loop
                break;

            } else {
                // Inicializando um novo padrão
                int[] newPattern = new int[quantityOfItems];

                // Looping através dos itens
                for (int i = 0; i < quantityOfItems; i++) {
                    // Gerando um novo padrão a partir da solução do problema da mochila
                    newPattern[i] = (int) knapsackVariables[i].solutionValue();
                }

                // Adicionando o novo padrão ao array de padrões
                patterns = Arrays.copyOf(patterns, patterns.length + 1);
                patterns[patterns.length - 1] = newPattern;
            }
        }

        calc.setCuttingPatterns(cuttingPatternRepository.findByCalc(calc));

        return calc;

    }

    /**
     * Sobrescreve o método toString para representar a solução do problema de corte em uma string formatada.
     * Se o problema foi resolvido, a string inclui os padrões de corte e o número de vezes que foram usados.
     * Se o problema não foi resolvido, a string informa que a solução não foi encontrada.
     *
     * @return A string representando a solução do problema.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        if(this.isSolved) {
            String border = "+---------------------+-----------------+-----------------+";
            String headerFormat = "| %-19s | %-15s | %-15s |\n";

            str.append(border).append("\n");
            str.append(String.format("| %-55s |\n", "Solution")).append(border).append("\n");
            str.append(String.format(headerFormat, "Cutting Pattern", "Times Used", "Waste"));
            str.append(border).append("\n");

            for (int i = 0; i < this.patterns.length; i++) {
                if (patternUsage[i] > 0) {
                    int waste = calculateWaste(patterns[i]);
                    str.append(String.format(headerFormat, Arrays.toString(patterns[i]), patternUsage[i], waste));
                }
            }

            str.append(border).append("\n");
        } else {
            str.append("Solution not found.");
        }

        return str.toString();
    }

    /**
     * Método para imprimir o objeto.
     * Imprime a string retornada pelo método toString.
     */
    public void print() {
        // Imprimindo a string retornada pelo método toString
        System.out.println(this);
    }

}