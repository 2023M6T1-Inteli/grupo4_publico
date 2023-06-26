import { Col, Row } from "antd";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

import "../styles/components-style.css";

import LeftCol from "../components/left-col";
import RightCol from "../components/right-col-";

const OrderIdPage = () => {
  const { id } = useParams();
  const [orderSet, setOrderSet] = useState({});

  useEffect(() => {
    let path = `http://localhost:8080/order-set/${id}`;
    axios.get(path).then((res) => {
      setOrderSet(res.data);
    });
  }, [id]);

  return (
    <>
      <Row style={{ alignItems: "flex-start" }}>
        <Col span={8} className="col-margin">
          <LeftCol orderSet={orderSet} orderSetId={id} />
        </Col>
        <Col span={14} className="calculation-list col-margin">
          <RightCol orderSet={orderSet} orderSetId={id} />
        </Col>
      </Row>
    </>
  );
};

export default OrderIdPage;
