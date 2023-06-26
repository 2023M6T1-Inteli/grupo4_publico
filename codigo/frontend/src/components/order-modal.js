import { InboxOutlined, PlusOutlined } from "@ant-design/icons";
import { Button, Form, Input, Modal, Upload, message } from "antd";
import React from "react";
import {
  CANCEL_BUTTON_TEXT,
  IMPORT_BUTTON_TEXT,
  INPUT_PLACEHOLDER,
  UPLOAD_TEXT,
  UPLODAD_HINT,
  SUCCESS_CREATE_MSG,
  ERROR_CREATE_MSG,
} from "../constants/order-modal";
import axios from "axios";

const { Dragger } = Upload;

const modalProps = {
  name: "file",
  multiple: false,
  action: "http://localhost:8080/order-set/upload",

  onchange(info) {
    const { status } = info.file;
    if (status !== "uploading") {
      console.log(info.file, info.fileList);
    }
    if (status === "done") {
      message.success(`${info.file.name} file uploaded successfully.`);
    } else if (status === "error") {
      message.error(`${info.file.name} file upload failed.`);
    }
  },

  ondrop(e) {
    console.log("Dropped files", e.dataTransfer.files);
  },
};

const OrderModal = (props) => {
  const onFinish = (values) => {
    const newValues = {
      name: values.name,
      fileName: values.fileName.file.name,
    };
    console.log(newValues);
    axios
      .post("http://localhost:8080/order-set", newValues)
      .then(() => {
        message.success(SUCCESS_CREATE_MSG);
      })
      .catch(() => {
        message.error(ERROR_CREATE_MSG);
      });
  };

  return (
    <>
      <Modal
        title="Importar pedidos"
        centered
        open={props.modalOpen}
        closable={false}
        footer={[]}
      >
        <Form onFinish={onFinish} layout="vertical">
          <Form.Item label="Nome do conjunto de pedidos" name="name">
            <Input
              placeholder={INPUT_PLACEHOLDER}
              style={{ marginBottom: "5px" }}
            />
          </Form.Item>

          <Form.Item name="fileName">
            <Dragger {...modalProps}>
              <p className="ant-upload-drag-icon">
                <InboxOutlined />
              </p>
              <p className="ant-upload-text">{UPLOAD_TEXT}</p>
              <p className="ant-upload-hint">{UPLODAD_HINT}</p>
            </Dragger>
          </Form.Item>

          <Form.Item>
            <Button key="back" onClick={() => props.handleCancel()}>
              {CANCEL_BUTTON_TEXT}
            </Button>
            <Button
              htmlType="submit"
              onClick={() => props.handleOk()}
              type="primary"
            >
              <PlusOutlined className="icon-style" />
              {IMPORT_BUTTON_TEXT}
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default OrderModal;
