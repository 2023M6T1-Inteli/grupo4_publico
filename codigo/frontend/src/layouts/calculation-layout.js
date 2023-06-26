import { Col, Row } from "antd";
import React from "react";
import { Outlet } from "react-router-dom";

const CalculationLayout = () => {
  return (
    <>
      <Row align="middle">
        <Col span={8} className="col-margin">
          <Outlet />
        </Col>
        <Col span={14} className="calculation-not-found col-margin">
          <Outlet />
        </Col>
      </Row>
    </>
  );
};

export default CalculationLayout;
