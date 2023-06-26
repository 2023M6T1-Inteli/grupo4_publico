package inteli.cc6.helpers;

import inteli.cc6.dtos.ItemDto;
import inteli.cc6.dtos.OrderDto;
import inteli.cc6.dtos.ResultDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class XlsxReader {

    public ResultDto readXlsxFile(InputStream inputStream) {
        Map<String, OrderDto> orderDtoMap = new HashMap<>();
        List<ItemDto> itemList = new ArrayList<>();

        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet orderSheet = workbook.getSheet("Orders");
            Iterator<Row> orderIterator = orderSheet.iterator();

            while (orderIterator.hasNext()) {

                Row currentRow = orderIterator.next();
                if (currentRow.getRowNum() == 0) { // skip header
                    continue;
                }

                Cell idCell = currentRow.getCell(0);
                Cell widthCell = currentRow.getCell(1);
                Cell amountCell = currentRow.getCell(2);

                if(idCell == null || widthCell == null || amountCell == null){
                    continue; // skip this row if any cell is null
                }

                String salesOrderId = idCell.toString();
                int width = (int) widthCell.getNumericCellValue();
                int amount = (int) amountCell.getNumericCellValue();

                OrderDto orderDto = orderDtoMap.get(salesOrderId);
                if (orderDto == null) {
                    orderDto = new OrderDto();
                    orderDto.setSalesOrderId(salesOrderId);
                    orderDto.setOrderSetId(UUID.randomUUID()); // Assign a random UUID to the order
                    orderDtoMap.put(salesOrderId, orderDto);
                }

                ItemDto itemDto = new ItemDto();
                itemDto.setWidth(width);
                itemDto.setAmount(amount);
                itemDto.setOrderId(orderDto.getOrderSetId());
                itemDto.setSalesOrderId(salesOrderId);

                itemList.add(itemDto);
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResultDto(new ArrayList<>(orderDtoMap.values()), itemList);
    }

    private static void printOrders(ResultDto result) {
        System.out.println("Total orders: " + result.getOrders().size());
        System.out.println("Total items: " + result.getItems().size());

        System.out.println("\nOrders:");
        for (OrderDto order : result.getOrders()) {
            System.out.println("Order ID: " + order.getSalesOrderId());
            System.out.println("Order UUID: " + order.getOrderSetId());
        }

        System.out.println("\nItems:");
        for (ItemDto item : result.getItems()) {
            System.out.println("Item Order UUID: " + item.getOrderId());
            System.out.println("Item Width: " + item.getWidth());
            System.out.println("Item Amount: " + item.getAmount());
        }
    }
}
