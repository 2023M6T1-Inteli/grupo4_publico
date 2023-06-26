package inteli.cc6.algorithm;

import inteli.cc6.models.*;
import inteli.cc6.repositories.CuttingPatternRepository;
import inteli.cc6.services.CalcService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class BranchAndPrice {

    CalcModel solvedCalModel;

    public BranchAndPrice(CalcService calcService, CalcModel calcModel, CuttingPatternRepository cuttingPatternRepository, OrderSetModel orderSet, MachineModel machine) {

        // Map to hold coil widths as keys and their summed amounts as values.
        Map<Integer, Integer> coilMap = new HashMap<>();

        // Extract coil widths and amounts from the order set.
        List<OrderModel> orders = orderSet.getOrders();
        for (OrderModel order : orders) {
            List<ItemModel> items = order.getItems();
            for (ItemModel item : items) {
                coilMap.put(item.getWidth(), coilMap.getOrDefault(item.getWidth(), 0) + item.getAmount());
            }
        }

        // Convert coilMap to arrays.
        int[] coilWidths = coilMap.keySet().stream().mapToInt(Integer::intValue).toArray();
        int[] coilAmounts = coilMap.values().stream().mapToInt(Integer::intValue).toArray();

        // Persisting calcModel coilWidths.
        calcModel.setWidth(Arrays.toString(coilWidths));
        calcModel = calcService.save(calcModel);

        // Machine's constraints
        int machineMinWidth = machine.getMinWidth();
        int machineMaxWidth = machine.getMaxWidth();
        int machineAvailableKnives = machine.getKnives();

        // Calcula a quantidade máxima permitida de bobinas por tirada.
        int maxCoilsPerDraw = machineAvailableKnives + 1;

        /** ORTools Solver */
        // Instanciando um solver completo usando a biblioteca ORTools.
        ORToolsSolver orToolsSolver = new ORToolsSolver(
                coilWidths,
                coilAmounts,
                machineMinWidth,
                machineMaxWidth,
                maxCoilsPerDraw,
                cuttingPatternRepository,
                calcModel);

        // Resolvendo o problema com esse solver.
        this.solvedCalModel = orToolsSolver.solve();

        // Imprimindo o resultado.
        orToolsSolver.print();
    }
}
