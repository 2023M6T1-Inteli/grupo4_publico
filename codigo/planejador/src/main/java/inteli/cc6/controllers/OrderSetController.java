package inteli.cc6.controllers;

import inteli.cc6.dtos.ResultDto;
import inteli.cc6.helpers.XlsxReader;
import inteli.cc6.dtos.OrderSetDto;
import inteli.cc6.models.OrderModel;
import inteli.cc6.models.OrderSetModel;
import inteli.cc6.services.OrderSetService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/order-set")
public class OrderSetController {

    private final OrderSetService orderSetService;
    private final XlsxReader xlsxReader;

    public OrderSetController(OrderSetService orderSetService, XlsxReader xlsxReader) {
        this.orderSetService = orderSetService;
        this.xlsxReader = xlsxReader;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid OrderSetDto orderSetDto) throws IOException {
        // First, create and save OrderSet.
        OrderSetModel orderSetModel = new OrderSetModel();
        BeanUtils.copyProperties(orderSetDto, orderSetModel);
        orderSetModel = orderSetService.save(orderSetModel);

        // Use the saved OrderSet's ID to fetch the associated file.
        byte[] fileBytes = orderSetService.getFileBytes(orderSetModel.getFileName());

        if (fileBytes == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file has been uploaded");
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
        ResultDto result = xlsxReader.readXlsxFile(byteArrayInputStream);

        // Now save Orders associated with the newly created OrderSet.
        orderSetService.saveOrders(result.getOrders(), orderSetModel);

        // For each order, save associated items
        for (OrderModel order : orderSetModel.getOrders()) {
            orderSetService.saveItems(result.getItems(order.getSalesOrderId()), order); // assuming that result.getItems() returns correct items for each order
        }

        return ResponseEntity.ok(orderSetService.getById(orderSetModel.getId()));
    }




    @GetMapping("/{id}")
    public ResponseEntity<OrderSetModel> read(@PathVariable UUID id) {
        return ResponseEntity.ok(orderSetService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderSetModel>> readAll() {
        return ResponseEntity.ok(orderSetService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid OrderSetDto orderSetDto) {
        OrderSetModel existingOrderSetModel = orderSetService.getById(id);

        if (existingOrderSetModel != null) {
            BeanUtils.copyProperties(orderSetDto, existingOrderSetModel, "createdAt");
            return ResponseEntity.ok(orderSetService.save(existingOrderSetModel));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        orderSetService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();

            String fileName = file.getOriginalFilename();

            orderSetService.handleFileUpload(fileName, bytes);

            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

}
