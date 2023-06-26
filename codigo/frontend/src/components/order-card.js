import { DeleteOutlined } from "@ant-design/icons";
import { Button, Card, Popconfirm, Typography, message } from "antd";
import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  POP_CONFIRM_DESCRIPTION,
  POP_CONFIRM_TITLE,
  SUCCESS_DELETE_MSG,
  ERROR_DELETE_MSG,
} from "../constants/order-card";
import "../styles/components-style.css";
import axios from "axios";

const { Title, Paragraph } = Typography;

const OrderCard = (props) => {
  const [formatedDate, setFormatedDate] = useState();
  const date = new Date(props.orderCreatedAt);
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  const hour = String(date.getHours()).padStart(2, "0");
  const minute = String(date.getMinutes()).padStart(2, "0");

  useEffect(() => {
    setFormatedDate(`${day}/${month}/${year} ${hour}:${minute}`);
  }, [formatedDate, day, month, year, hour, minute]);

  const handleConfirm = (e) => {
    let path = `http://localhost:8080/order-set/${props.orderId}`;
    axios
      .delete(path)
      .then((res) => {
        message.success(SUCCESS_DELETE_MSG);
      })
      .catch(() => {
        message.error(ERROR_DELETE_MSG);
      });
  };
  const handleCancel = (e) => {
    console.log(e);
    message.warning("Clicou para não excluir");
  };

  return (
    <>
      <div className="order-card">
        <Card size="small">
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <div>
              <Link to={`/order/id/${props.orderId}`}>
                <Button type="text">
                  <Title
                    level={5}
                    style={{ marginTop: "0", textAlign: "initial" }}
                  >
                    {props.orderName}
                  </Title>
                </Button>
              </Link>
              <Paragraph
                style={{
                  textAlign: "initial",
                  marginBottom: "0",
                  paddingLeft: "15px",
                  border: "1",
                }}
              >
                {formatedDate}
              </Paragraph>
            </div>

            <Popconfirm
              title={POP_CONFIRM_TITLE}
              description={POP_CONFIRM_DESCRIPTION}
              onConfirm={handleConfirm}
              onCancel={handleCancel}
              okText="Deletar"
              cancelText="Cancelar"
            >
              <Button type="text">
                <DeleteOutlined />
              </Button>
            </Popconfirm>
          </div>
        </Card>
      </div>
    </>
  );
};

export default OrderCard;
