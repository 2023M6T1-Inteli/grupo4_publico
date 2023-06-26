import { Button, Form, Input, Modal } from "antd";
import React from "react";
import axios from "axios";

const MachineModal = (props) => {
  const onFinish = (values) => {
    axios
      .post("http://localhost:8080/machine", values)
      .then((res) => {
        console.log(res.data);
        props.setMachines([...props.machines, res.data]);
      })
      .catch((res) => {
        console.log(res);
      });
  };

  return (
    <>
      <Modal
        title="Adicionar nova máquina"
        centered
        open={props.modalOpen}
        closable={false}
        footer={[]}
      >
        <Form name="basic" onFinish={onFinish}>
          <Form.Item
            label="Nome"
            name="name"
            rules={[
              {
                required: true,
                message: "Insira o nome da máquina!",
              },
            ]}
          >
            <Input placeholder="Noma da máquina" />
          </Form.Item>

          <Form.Item
            label="Largura mínima"
            name="minWidth"
            rules={[
              {
                required: true,
                message: "Insira a largura mínima da máquina!",
              },
            ]}
          >
            <Input placeholder="Largura da máquina" />
          </Form.Item>

          <Form.Item
            label="Largura máxima"
            name="maxWidth"
            rules={[
              {
                required: true,
                message: "Insira a largura máxima da máquina!",
              },
            ]}
          >
            <Input placeholder="Largura da máquina" />
          </Form.Item>

          <Form.Item
            label="Quantidade de facas"
            name="knives"
            rules={[
              {
                required: true,
                message: "Insira a quantidade de facas da máquina!",
              },
            ]}
          >
            <Input placeholder="Quantidade de facas da máquina" />
          </Form.Item>

          <Form.Item style={{ textAlign: "right" }}>
            <Button
              key="back"
              onClick={() => {
                props.handleCancel();
              }}
            >
              Cancelar
            </Button>

            <Button
              type="primary"
              htmlType="submit"
              onClick={() => {
                props.handleOk();
              }}
            >
              Adicionar máquina
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default MachineModal;
