import { PlusOutlined } from "@ant-design/icons";
import { Button, Typography, message } from "antd";
import React, { useEffect, useState } from "react";
import OrderCard from "../components/order-card";
import OrderModal from "../components/order-modal";
import UpperNavBar from "../components/upper-nav-bar";
import { IMPORT_BUTTON_TEXT, INFO_TEXT } from "../constants/orders-page";
import "../styles/components-style.css";
import axios from "axios";


const { Paragraph } = Typography;

const OrdersPage = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/order-set").then((res) => {
      setOrders(res.data);
    });
  }, []);

  const [modalOpen, setModalOpen] = useState(false);

  const handleCancel = () => {
    message.warning("Pedido não adicionado");
    setModalOpen(false);
  };
  const handleOk = () => {
    setModalOpen(false);
  };

  return (
    <>
      <UpperNavBar />

      <Button
        className="button-default"
        type="primary"
        onClick={() => {
          setModalOpen(true);
        }}
        block
      >
        <PlusOutlined className="icon-style" />
        {IMPORT_BUTTON_TEXT}
      </Button>
      <Paragraph style={{ marginTop: "15px" }}>{INFO_TEXT}</Paragraph>

      <OrderModal
        modalOpen={modalOpen}
        handleCancel={handleCancel}
        handleOk={handleOk}
      />

      {orders.map((card) => {
        return (
          <OrderCard
            key={card.id}
            orderId={card.id}
            orderName={card.name}
            orderCreatedAt={card.createdAt}
          />
        );
      })}
    </>
  );
};

export default OrdersPage;
