/**
 * Classe Simplex para a resolução de problemas de programação linear utilizando o algoritmo Simplex.
 *
 * @author Elias Biondo
 * @version 1.0
 * @since 2023-05-26
 */
package inteli.cc6.deprecated;

import java.util.ArrayList;
import java.util.Arrays;

public class SimplexSimpleSolver {
    private static final double EPSILON = 1.0E-8;
    private final double[][] aMatrix;
    private final int[] augmentedMatrix;
    private final double[] coilAmounts;
    private final double[][] objectiveFunctionCoefficients;
    private final int numberOfConstrains;
    private final int numberOfVariables;
    private int[] basis;
    private boolean isSolved = false;


    /**
     * Construtor para a classe Simplex que é usado para resolver problemas de programação linear em otimização de corte de bobina (Cutting Stock Problem).
     * Este construtor espera os tamanhos das bobinas, a quantidade de cada tamanho de bobina e uma lista de padrões de corte normalizados.
     *
     * @param coilWidths  Um array de inteiros que representa os tamanhos das bobinas.
     * @param coilAmounts Um array de inteiros que representa a quantidade necessária de cada tamanho de bobina.
     * @param normalizedCuttingPatternList Uma lista de listas de inteiros que representam os padrões de corte normalizados.
     */
    public SimplexSimpleSolver(int[] coilWidths, int[] coilAmounts, ArrayList<ArrayList<Integer>> normalizedCuttingPatternList) {

        // Instancia os coeficientes da função objetivo. Todos são definidos como 1.0 pois estamos tentando minimizar o número de bobinas utilizadas.
        double[] c = new double[coilWidths.length];
        Arrays.fill(c, 1.0);

        // Instancia os coeficientes do lado esquerdo das restrições (LHS - Left Hand Side), que são os padrões de corte.
        this.aMatrix = new double[normalizedCuttingPatternList.size()][normalizedCuttingPatternList.size()];
        setCuttingPatternsFrom(normalizedCuttingPatternList);

        // Atribui os valores à direita e à esquerda das restrições e os coeficientes da função objetivo às variáveis da classe.
        this.augmentedMatrix = coilAmounts;
        this.coilAmounts = c;

        // Define o número de restrições.
        this.numberOfConstrains = augmentedMatrix.length;
        // Define o número de variáveis.
        this.numberOfVariables = c.length;

        // Inicializa a matriz de tabela simplex usada para resolver o problema de programação linear.
        this.objectiveFunctionCoefficients = new double[numberOfConstrains+2][numberOfVariables+numberOfConstrains+numberOfConstrains+1];
    }



    /**
     * Primeira fase do algoritmo simplex.
     * Este método realiza as operações básicas para a formação da matriz tableau, e verifica a viabilidade do programa linear.
     * Caso o programa não seja viável, lança uma ArithmeticException.
     */
    private void phase1() {
        // Loop principal da fase 1.
        while (true) {
            // Usa a regra de Bland para escolher a coluna de entrada q.
            int q = bland1();
            // Se q for -1, todos os coeficientes da linha de custos são não negativos, quebrando o loop.
            if (q == -1) break;

            // Usa a regra de proporção mínima para escolher a linha de saída p.
            int p = minRatioRule(q);
            // Confirma se p não é -1.
            assert p != -1 : "Entering column = " + q;

            // Realiza a operação de pivotamento na célula a[p][q].
            pivot(p, q);

            // Atualiza a base.
            basis[p] = q;
        }

        // Se o valor objetivo da fase 1 é maior que um pequeno valor positivo (EPSILON),
        // lança uma exceção indicando que o programa linear é inviável.
        if (objectiveFunctionCoefficients[numberOfConstrains+1][numberOfVariables+numberOfConstrains+numberOfConstrains] > EPSILON) throw new ArithmeticException("Linear program is infeasible");
    }

    /**
     * Segunda fase do algoritmo simplex.
     * Este método realiza as operações básicas para a otimização da função objetivo.
     * Caso o programa seja ilimitado, lança uma ArithmeticException.
     */
    private void phase2() {
        // Loop principal da fase 2.
        while (true) {
            // Usa a regra de Bland para escolher a coluna de entrada q.
            int q = bland2();
            // Se q for -1, todos os coeficientes da linha de custos são não negativos, quebrando o loop.
            if (q == -1) break;

            // Usa a regra de proporção mínima para escolher a linha de saída p.
            int p = minRatioRule(q);
            // Se p for -1, lança uma exceção indicando que o programa linear é ilimitado.
            if (p == -1) throw new ArithmeticException("Linear program is unbounded");

            // Realiza a operação de pivotamento na célula a[p][q].
            pivot(p, q);

            // Atualiza a base.
            basis[p] = q;
        }
    }


    /**
     * Regra de Bland para evitar ciclos durante a fase 1.
     * Identifica a coluna que será utilizada para o pivotamento na primeira fase do algoritmo simplex.
     * Retorna -1 caso não haja colunas válidas para pivotamento.
     */
    private int bland1() {
        // Itera sobre as colunas até encontrar uma com coeficiente positivo na linha de custos da fase 1.
        for (int j = 0; j < numberOfVariables+numberOfConstrains; j++)
            // Se o coeficiente na linha de custos da fase 1 é maior que um pequeno valor positivo (EPSILON), retorna o índice da coluna.
            if (objectiveFunctionCoefficients[numberOfConstrains+1][j] > EPSILON) return j;
        // Se nenhuma coluna válida é encontrada, retorna -1.
        return -1;
    }

    /**
     * Regra de Bland para evitar ciclos durante a fase 2.
     * Identifica a coluna que será utilizada para o pivotamento na segunda fase do algoritmo simplex.
     * Retorna -1 caso não haja colunas válidas para pivotamento.
     */
    private int bland2() {
        // Itera sobre as colunas até encontrar uma com coeficiente positivo na linha de custos da fase 2.
        for (int j = 0; j < numberOfVariables+numberOfConstrains; j++)
            // Se o coeficiente na linha de custos da fase 2 é maior que um pequeno valor positivo (EPSILON), retorna o índice da coluna.
            if (objectiveFunctionCoefficients[numberOfConstrains][j] > EPSILON) return j;
        // Se nenhuma coluna válida é encontrada, retorna -1.
        return -1;
    }

    /**
     * Implementa a regra do mínimo quociente para a seleção da linha pivot.
     * Retorna o índice da linha que será usada para pivotamento.
     * Retorna -1 caso não haja linhas válidas para pivotamento.
     */
    private int minRatioRule(int q) {
        // Inicializa p como -1 para indicar que nenhuma linha válida foi encontrada ainda.
        int p = -1;
        // Itera sobre as linhas.
        // Ignora a linha se o coeficiente na coluna q é pequeno ou negativo.
        for (int i = 0; i < numberOfConstrains; i++)
                // Se p ainda é -1, atualiza p para ser a linha atual.
            if (p == -1) p = i;
                // Se o quociente da linha atual é menor que o quociente da linha p, atualiza p para ser a linha atual.
            else if ((objectiveFunctionCoefficients[i][numberOfVariables + numberOfConstrains + numberOfConstrains] / objectiveFunctionCoefficients[i][q]) < (objectiveFunctionCoefficients[p][numberOfVariables + numberOfConstrains + numberOfConstrains] / objectiveFunctionCoefficients[p][q]))
                p = i;
        // Retorna o índice da linha p para pivotamento ou -1 se nenhuma linha válida foi encontrada.
        return p;
    }

    /**
     * Realiza a operação de pivotamento na matriz tableau.
     * O pivotamento é realizado na linha p e coluna q.
     */
    private void pivot(int p, int q) {
        // Itera sobre cada célula da matriz.
        for (int i = 0; i <= numberOfConstrains+1; i++)
            for (int j = 0; j <= numberOfVariables+numberOfConstrains+numberOfConstrains; j++)
                // Se a célula não está na linha ou coluna do pivot, atualiza o valor da célula de acordo com a regra do pivotamento.
                if (i != p && j != q) objectiveFunctionCoefficients[i][j] -= objectiveFunctionCoefficients[p][j] * objectiveFunctionCoefficients[i][q] / objectiveFunctionCoefficients[p][q];

        // Zera todos os elementos na coluna do pivot, exceto o próprio pivot.
        for (int i = 0; i <= numberOfConstrains+1; i++)
            if (i != p) objectiveFunctionCoefficients[i][q] = 0.0;

        // Divide todos os elementos na linha do pivot pelo valor do pivot, tornando o valor do pivot igual a 1.
        for (int j = 0; j <= numberOfVariables+numberOfConstrains+numberOfConstrains; j++)
            if (j != q) objectiveFunctionCoefficients[p][j] /= objectiveFunctionCoefficients[p][q];
        objectiveFunctionCoefficients[p][q] = 1.0;
    }

    /**
     * Retorna o valor ótimo da função objetivo.
     *
     * @return Valor ótimo da função objetivo.
     */
    public double value() {
        // Retorna o valor negativo da última célula na linha da função objetivo (representa o valor ótimo da função objetivo).
        return -objectiveFunctionCoefficients[numberOfConstrains][numberOfVariables+numberOfConstrains+numberOfConstrains];
    }

    /**
     * Retorna a solução primal ótima.
     *
     * @return Vetor com a solução primal ótima.
     */
    public double[] primal() {
        // Inicializa o vetor de solução primal.
        double[] x = new double[numberOfVariables];
        // Itera sobre a base atual.
        for (int i = 0; i < numberOfConstrains; i++)
            // Se o índice da base é menor que o número de variáveis, atualiza o valor correspondente no vetor de solução primal.
            if (basis[i] < numberOfVariables) x[basis[i]] = objectiveFunctionCoefficients[i][numberOfVariables+numberOfConstrains+numberOfConstrains];
        // Retorna o vetor de solução primal.
        return x;
    }

    /**
     * Retorna a solução dual ótima.
     *
     * @return Vetor com a solução dual ótima.
     */
    public double[] dual() {
        // Inicializa o vetor de solução dual.
        double[] y = new double[numberOfConstrains];
        // Itera sobre a base atual.
        for (int i = 0; i < numberOfConstrains; i++)
            // Atualiza o valor correspondente no vetor de solução dual.
            y[i] = -objectiveFunctionCoefficients[numberOfConstrains][numberOfVariables+i];
        // Retorna o vetor de solução dual.
        return y;
    }


    /**
     * Verifica se a solução primal é viável.
     *
     * @param A Matriz de coeficientes das restrições.
     * @param b Vetor de valores à direita das restrições.
     *
     * @return true se a solução primal é viável, false caso contrário.
     */
    private boolean isPrimalFeasible(double[][] A, double[] b) {
        // Obtém a solução primal ótima.
        double[] x = primal();

        // Verifica se todas as variáveis da solução primal são não-negativas.
        for (int j = 0; j < x.length; j++) {
            if (x[j] < 0.0) {
                System.out.println("x[" + j + "] = " + x[j] + " is negative");
                return false;
            }
        }

        // Verifica se todas as restrições são satisfeitas pela solução primal.
        for (int i = 0; i < numberOfConstrains; i++) {
            double sum = 0.0;
            // Calcula a soma ponderada dos valores da solução primal de acordo com a matriz A.
            for (int j = 0; j < numberOfVariables; j++) {
                sum += A[i][j] * x[j];
            }
            // Se a soma for maior que o valor à direita da restrição, a solução primal não é viável.
            if (sum > b[i] + EPSILON) {
                System.out.println("not primal feasible");
                System.out.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
                return false;
            }
        }
        // Se todas as variáveis são não-negativas e todas as restrições são satisfeitas, a solução primal é viável.
        return true;
    }


    /**
     * Verifica se a solução dual é viável.
     *
     * @param A Matriz de coeficientes das restrições.
     * @param c Vetor de coeficientes da função objetivo.
     *
     * @return true se a solução dual é viável, false caso contrário.
     */
    private boolean isDualFeasible(double[][] A, double[] c) {
        // Obtém a solução dual ótima.
        double[] y = dual();

        // Verifica se todas as variáveis da solução dual são não-negativas.
        // Uma solução dual não é viável se uma variável é negativa.
        for (int i = 0; i < y.length; i++) {
            if (y[i] < 0.0) {
                System.out.println("y[" + i + "] = " + y[i] + " is negative");
                return false;
            }
        }

        // Verifica se a solução dual satisfez todas as restrições duais.
        // A solução dual não é viável se a soma ponderada dos valores da solução dual é menor que o coeficiente correspondente da função objetivo.
        for (int j = 0; j < numberOfVariables; j++) {
            double sum = 0.0;
            // Calcula a soma ponderada dos valores da solução dual de acordo com a matriz A.
            for (int i = 0; i < numberOfConstrains; i++) {
                sum += A[i][j] * y[i];
            }
            // Se a soma for menor que o coeficiente correspondente da função objetivo, a solução dual não é viável.
            if (sum < c[j] - EPSILON) {
                System.out.println("not dual feasible");
                System.out.println("c[" + j + "] = " + c[j] + ", sum = " + sum);
                return false;
            }
        }
        // Se todas as variáveis são não-negativas e todas as restrições duais são satisfeitas, a solução dual é viável.
        return true;
    }


    /**
     * Verifica se a solução atual é ótima.
     *
     * @param b Vetor de valores à direita das restrições.
     * @param c Vetor de coeficientes da função objetivo.
     *
     * @return true se a solução atual é ótima, false caso contrário.
     */
    private boolean isOptimal(double[] b, double[] c) {
        // Obtém a solução primal e dual ótima, e o valor da função objetivo.
        double[] x = primal();  // Solução primal.
        double[] y = dual();  // Solução dual.
        double value = value();  // Valor da função objetivo.

        // Calcula o produto interno dos coeficientes da função objetivo (c) e a solução primal (x).
        double value1 = 0.0;
        for (int j = 0; j < x.length; j++)
            value1 += c[j] * x[j];  // Calcula o somatório de c[j] * x[j] para todo j.

        // Calcula o produto interno dos valores à direita das restrições (b) e a solução dual (y).
        double value2 = 0.0;
        for (int i = 0; i < y.length; i++)
            value2 += y[i] * b[i];  // Calcula o somatório de y[i] * b[i] para todo i.

        // Verifica se o valor da função objetivo é igual aos dois produtos internos calculados anteriormente, com uma tolerância de erro (EPSILON).
        if (Math.abs(value - value1) > EPSILON || Math.abs(value - value2) > EPSILON) {
            // Se o valor da função objetivo é diferente de qualquer um dos produtos internos (dado a tolerância), a solução atual não é ótima.
            System.out.println("value = " + value + ", cx = " + value1 + ", yb = " + value2);
            return false;  // Retorna false indicando que a solução atual não é ótima.
        }

        return true;  // Se o valor da função objetivo é igual aos dois produtos internos (dado a tolerância), a solução atual é ótima e retorna true.
    }


    /**
     * Verifica se a solução encontrada é viável e ótima.
     * Esta verificação é feita verificando se as soluções primal e dual são viáveis, e se a função objetivo é otimizada.
     * Retorna true se a solução é viável e ótima, caso contrário, retorna false.
     */
    private boolean check(double[][]A, double[] b, double[] c) {
        return isPrimalFeasible(A, b) && isDualFeasible(A, c) && isOptimal(b, c);
    }

    /**
     * Exibe a solução atual.
     */
    public void show() {
        // Imprime o número de restrições (m).
        System.out.println("m = " + numberOfConstrains);
        // Imprime o número de variáveis originais (n).
        System.out.println("n = " + numberOfVariables);

        // Loop para imprimir todos os elementos da matriz tableau a.
        for (int i = 0; i <= numberOfConstrains+1; i++) {
            for (int j = 0; j <= numberOfVariables+numberOfConstrains+numberOfConstrains; j++) {
                // Imprime cada elemento da matriz a com 2 casas decimais.
                System.out.printf("%7.2f ", objectiveFunctionCoefficients[i][j]);

                // Adiciona uma barra vertical "|" para separar diferentes partes da matriz a.
                if (j == numberOfVariables+numberOfConstrains-1 || j == numberOfVariables+numberOfConstrains+numberOfConstrains-1) System.out.print(" |");
            }
            // Passa para a próxima linha após imprimir todos os elementos de uma linha.
            System.out.println();
        }

        // Imprime a base atual da solução.
        System.out.print("basis = ");
        for (int i = 0; i < numberOfConstrains; i++)
            // Imprime cada elemento da base.
            System.out.print(basis[i] + " ");

        // Adiciona uma nova linha no final para separar diferentes chamadas de "show()".
        System.out.println();
        System.out.println();
    }


    /**
     * Método de teste para a classe Simplex.
     */
    public double solve() {

        // Cria um vetor bd de mesmo tamanho que c para armazenar os valores de b como doubles.
        double[] bd = new double[coilAmounts.length];

        // Converte cada elemento do vetor b em double e armazena em bd.
        for(int i = 0; i < coilAmounts.length; i++) {
            bd[i] = (float) augmentedMatrix[i];
        }

        // Copia a matriz de restrições A para a tabela.
        for (int i = 0; i < numberOfConstrains; i++)
            if (numberOfVariables >= 0)
                System.arraycopy(aMatrix[i], 0, objectiveFunctionCoefficients[i], 0, numberOfVariables);

        // Adiciona colunas para as variáveis de folga.
        for (int i = 0; i < numberOfConstrains; i++)
            objectiveFunctionCoefficients[i][numberOfVariables+i] = 1.0;

        // Copia o vetor b (valores à direita das restrições) para a última coluna da tabela.
        for (int i = 0; i < numberOfConstrains; i++)
            objectiveFunctionCoefficients[i][numberOfVariables+numberOfConstrains+numberOfConstrains] = augmentedMatrix[i];

        // Copia o vetor de função objetivo para a última linha da tabela.
        if (numberOfVariables >= 0)
            System.arraycopy(coilAmounts, 0, objectiveFunctionCoefficients[numberOfConstrains], 0, numberOfVariables);

        // Muda o sinal da linha se o valor à direita é negativo.
        for (int i = 0; i < numberOfConstrains; i++) {
            if (augmentedMatrix[i] < 0) {
                for (int j = 0; j <= numberOfVariables+numberOfConstrains+numberOfConstrains; j++)
                    objectiveFunctionCoefficients[i][j] = -objectiveFunctionCoefficients[i][j];
            }
        }

        // Adiciona colunas para as variáveis artificiais.
        for (int i = 0; i < numberOfConstrains; i++)
            objectiveFunctionCoefficients[i][numberOfVariables+numberOfConstrains+i] = 1.0;

        // Seta os coeficientes das variáveis artificiais na linha do objetivo da fase I para -1.
        for (int i = 0; i < numberOfConstrains; i++)
            objectiveFunctionCoefficients[numberOfConstrains+1][numberOfVariables+numberOfConstrains+i] = -1.0;

        // Pivoteia as linhas na base das variáveis artificiais.
        for (int i = 0; i < numberOfConstrains; i++)
            pivot(i, numberOfVariables+numberOfConstrains+i);

        // Cria e preenche o vetor da base inicial com as variáveis artificiais.
        basis = new int[numberOfConstrains];
        for (int i = 0; i < numberOfConstrains; i++)
            basis[i] = numberOfVariables + numberOfConstrains + i;

        // Executa a fase I do Simplex para remover as variáveis artificiais.
        phase1();

        // Executa a fase II do Simplex para encontrar a solução ótima.
        phase2();

        // Verifica se a solução ótima encontrada é factível e ótima.
        assert check(aMatrix, bd, coilAmounts);

        // Tenta criar um novo objeto Simplex com os coeficientes das restrições (A), os valores à direita das restrições (bd) e os coeficientes da função objetivo (c).
        try {
            this.isSolved = true;
            return this.value();
        }
        // Captura qualquer exceção aritmética que possa ocorrer durante a criação do objeto Simplex.
        catch (ArithmeticException e) {
            return -1;
        }

    }

    /**
     * Este método retorna uma representação em string do Simplex.
     * Se o Simplex foi resolvido, ele imprime uma tabela com cabeçalhos de colunas e os valores das variáveis.
     * Se o Simplex não foi resolvido, retorna uma string indicando que a solução não foi encontrada.
     *
     * @return Uma string representando o Simplex. Inclui uma tabela de valores variáveis se o Simplex foi resolvido.
     *         Se o Simplex não foi resolvido, retorna "Solution not found."
     */
    public String toString() {
        StringBuilder str = new StringBuilder();

        if(this.isSolved) {
            // Column headers
            str.append("+-----+-------+\n");
            str.append("    Simplex   \n");
            str.append("+-----+-------+\n");
            str.append("| Var | Value |\n");
            str.append("+-----+-------+\n");

            for (int i = 1; i <= this.primal().length; i++) {
                str.append("| x").append(i).append("  | ").append(String.format("%.2f", this.primal()[i - 1])).append(" |\n");
                str.append("+-----+-------+\n");
            }
        } else {
            str.append("Solution not found.");
        }
        return str.toString();

    }

    /**
     * Este método imprime a representação em string do Simplex utilizando o método toString.
     * Se o Simplex foi resolvido, imprimirá uma tabela com cabeçalhos de colunas e os valores das variáveis.
     * Se o Simplex não foi resolvido, imprimirá "Solution not found."
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Configura os padrões de corte a partir de uma lista de padrões de corte normalizados.
     *
     * @param normalizedCuttingPatternList uma lista de padrões de corte normalizados.
     */
    private void setCuttingPatternsFrom(ArrayList<ArrayList<Integer>> normalizedCuttingPatternList) {
        for(int i = 0; i < normalizedCuttingPatternList.size(); i++) {
            for(int j = 0; j < normalizedCuttingPatternList.get(i).size(); j++) {
                this.aMatrix[i][j] = normalizedCuttingPatternList.get(i).get(j);
            }
        }
    }


}
