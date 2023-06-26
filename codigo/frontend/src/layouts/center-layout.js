import { Col, Row } from "antd";
import React from "react";
import { Outlet } from "react-router-dom";

const CenterLayout = () => {
  return (
    <>
      <Row justify="center" align="middle" style={{ height: "100vh" }}>
        <Col xs={24} sm={12} md={8} lg={6}>
          <div className="center-content">
            <Outlet />
          </div>
        </Col>
      </Row>
    </>
  );
};

export default CenterLayout;
