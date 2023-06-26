import { ArrowLeftOutlined } from "@ant-design/icons";
import { Button, Form, Input, message } from "antd";
import React, { useState } from "react";
import { Link } from "react-router-dom";
import MachinesToSelected from "./machines-to-selected";
import MainPageTitle from "../components/main-page-title";
import OrdersTable from "../components/orders-table";
import axios from "axios";

const LeftCol = (props) => {
  const [machineValue, setMachineValue] = useState();

  const orders = props.orderSet?.orders;
  const items = [];
  orders?.forEach((order) => {
    const orderId = order.salesOrderId;
    order.items.forEach((item) => {
      const newItem = {
        ...item,
        key: item.id,
        salesOrderId: orderId,
      };
      items.push(newItem);
    });
  });

  const onFinish = (values) => {
    const calcValues = {
      ...values,
      orderSetId: props.orderSetId,
      machineId: machineValue,
    };
    console.log(calcValues);

    axios
      .post("http://localhost:8080/calc", calcValues)
      .then((res) => {
        message.info("Realizando cálculo...");
      })
      .catch((err) => {
        message.error("Não foi possível iniciar o cálculo");
      });
  };

  return (
    <>
      <Link to="/orders">
        <Button type="text">
          <ArrowLeftOutlined style={{ marginRight: "5px" }} />
          <b>Voltar</b>
        </Button>
      </Link>

      <div style={{ marginLeft: "20px" }}>
        <MainPageTitle title={props.orderSet.name} />
        <OrdersTable data={items} />
        <MachinesToSelected setMachineValue={setMachineValue} />

        <Form onFinish={onFinish}>
          <Form.Item label="Nome do cálculo" name="name">
            <Input placeholder="Nome do cálculo"></Input>
          </Form.Item>
          <Form.Item>
            <Button
              type="primary"
              block
              className="margin-top-high"
              htmlType="submit"
            >
              Calcular
            </Button>
          </Form.Item>
        </Form>
      </div>
    </>
  );
};

export default LeftCol;
