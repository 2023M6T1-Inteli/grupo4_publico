/**
 * Esta classe é responsável por realizar a otimização linear utilizando o método Simplex.
 * Foi projetada para resolver o problema do corte de bobinas dadas as larguras, quantidades e padrões iniciais de corte.
 *
 * @author Elias Biondo
 * @version 1.0
 * @since 2023-05-26
 */
package inteli.cc6.deprecated;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.linear.SimplexSolver;

import java.util.ArrayList;
import java.util.Collection;

public class ApacheCommonsSimpleSolver {

    private int[] coilWidths;
    private int[] coilAmounts;
    private double[][] cuttingPatterns;
    private Collection<LinearConstraint> constraints;
    private double[] objective;
    private LinearObjectiveFunction f;
    private PointValuePair solution;
    private boolean solved = false;

    /**
     * Inicializa uma nova instância de ApacheCommonsSimpleSolver.
     * Configura o problema baseado nas larguras de bobina, quantidades e padrões de corte fornecidos.
     *
     * @param coilWidths um array de inteiros que representa as larguras das bobinas.
     * @param coilAmounts um array de inteiros que representa as quantidades de cada bobina.
     * @param normalizedCuttingPatternList uma lista de padrões de corte normalizados.
     */
    public ApacheCommonsSimpleSolver(int[] coilWidths, int[] coilAmounts, ArrayList<ArrayList<Integer>> normalizedCuttingPatternList) {

        this.coilWidths = coilWidths;
        this.coilAmounts = coilAmounts;
        this.cuttingPatterns = new double[coilWidths.length][coilWidths.length];
        this.constraints = new ArrayList<LinearConstraint>();
        this.objective = new double[coilWidths.length];

        setCuttingPatternsFrom(normalizedCuttingPatternList);
        setConstraints();
        setObjective();
        setFunction();


    }

    /**
     * Configura os padrões de corte a partir de uma lista de padrões de corte normalizados.
     *
     * @param normalizedCuttingPatternList uma lista de padrões de corte normalizados.
     */
    private void setCuttingPatternsFrom(ArrayList<ArrayList<Integer>> normalizedCuttingPatternList) {
        for(int i = 0; i < normalizedCuttingPatternList.size(); i++) {
            for(int j = 0; j < normalizedCuttingPatternList.get(i).size(); j++) {
                this.cuttingPatterns[i][j] = normalizedCuttingPatternList.get(i).get(j);
            }
        }
    }

    /**
     * Configura as restrições para o problema de otimização.
     */
    private void setConstraints() {
        for(int i = 0; i < this.cuttingPatterns.length; i++) {
            this.constraints.add(new LinearConstraint(this.cuttingPatterns[i], Relationship.GEQ, this.coilAmounts[i]));
        }
    }

    /**
     * Configura o objetivo para o problema de otimização.
     */
    private void setObjective() {
        for(int i = 0; i < this.coilWidths.length; i++) {
            this.objective[i] = 1;
        }
    }

    /**
     * Configura a função para o problema de otimização.
     */
    private void setFunction() {
        this.f = new LinearObjectiveFunction(this.objective, 0);
    }

    /**
     * Resolve o problema de otimização.
     *
     * @return o valor da solução otimizada.
     */
    public double solve() {
        SimplexSolver solver = new SimplexSolver();
        this.solution = solver.optimize(this.f, this.constraints, GoalType.MINIMIZE, true);
        this.solved = true;
        return solution.getValue();
    }

    /**
     * Retorna o ponto da solução otimizada.
     *
     * @return um array de double que representa o ponto da solução.
     * @throws Exception se o problema ainda não foi resolvido.
     */
    public double[] getPoint() throws Exception {
        if(this.solved) {
            return solution.getPoint();
        } else {
            throw new Exception("Problem is not solved yet. Unable to get point.");
        }
    }

    /**
     * Retorna uma representação de string da solução.
     *
     * @return uma representação de string da solução.
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        if(this.solved) {
            // Column headers
            str.append("+-----+-------+\n");
            str.append("  ApacheCommon  \n");
            str.append("+-----+-------+\n");
            str.append("| Var | Value |\n");
            str.append("+-----+-------+\n");

            for (int i = 1; i <= this.solution.getPoint().length; i++) {
                str.append("| x" + i + "  | " + String.format("%.2f", this.solution.getPoint()[i-1]) + " |\n");
                str.append("+-----+-------+\n");
            }
        } else {
            str.append("Solution not found.");
        }
        return str.toString();
    }

    public void print() {
        System.out.println(this.toString());
    }


}
