import { DeleteOutlined } from "@ant-design/icons";
import { Button, Card, Popconfirm, Typography, message } from "antd";
import React from "react";
import {
  SUCCESS_DELETE_MSG,
  WARNING_DELETE_MSG,
  ERROR_DELETE_MSG,
} from "../constants/machine-card";
import "../styles/components-style.css";
import axios from "axios";

const { Title, Paragraph } = Typography;

const MachineCard = (props) => {
  const handleConfirm = (e) => {
    console.log(e);
    let path = `http://localhost:8080/machine/${props.id}`;
    axios
      .delete(path)
      .then((res) => {
        message.success(SUCCESS_DELETE_MSG);
      })
      .catch((err) => {
        message.error(ERROR_DELETE_MSG);
      });
  };
  const handleCancel = (e) => {
    console.log(e);
    message.warning(WARNING_DELETE_MSG);
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
              <Title
                level={5}
                style={{
                  marginTop: "0",
                  textAlign: "initial",
                  paddingLeft: "15px",
                  border: "1",
                  marginBottom: "5px",
                }}
              >
                {props.machineName}
              </Title>
              <Paragraph
                style={{
                  textAlign: "initial",
                  marginBottom: "0",
                  paddingLeft: "15px",
                  border: "1",
                }}
              >
                {props.machineMaxwidth} mm, {props.machineKnives} facas
              </Paragraph>
            </div>

            <Popconfirm
              title="Deletar máquina"
              description="Deseja deletar a máquina?"
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

export default MachineCard;
