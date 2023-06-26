import { Table } from "antd";
import Column from "antd/es/table/Column";
import React from "react";

const OrdersTable = (props) => {
  return (
    <>
      <div className="margin-top-high">
        <Table dataSource={props.data} pagination={{ pageSize: "5" }}>
          <Column
            title="Ordem de venda"
            dataIndex="salesOrderId"
            key="salesOrderId"
          />
          <Column title="Largura" dataIndex="width" key="width" />
          <Column
            title="Quantidade de bobinas"
            dataIndex="amount"
            key="amount"
          />
        </Table>
      </div>
    </>
  );
};

export default OrdersTable;
