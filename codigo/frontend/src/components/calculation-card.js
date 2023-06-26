import { RightOutlined } from "@ant-design/icons";
import { Card, Typography } from "antd";
import React from "react";
import { Link, useLocation } from "react-router-dom";

const { Title } = Typography;

const CalculationCard = (props) => {
  const location = useLocation().pathname;

  return (
    <>
      <Link to={`${location}/calc/id/${props.calcId}`}>
        <Card size="small" hoverable block>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <div style={{ display: "flex", alignItems: "center" }}>
              <Title
                style={{ marginTop: "0", marginBottom: 0, marginLeft: "5px" }}
                level={5}
              >
                {props.calcName}
              </Title>
            </div>
            <div
              style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <RightOutlined style={{ marginRight: "5px" }} />
            </div>
          </div>
        </Card>
      </Link>
    </>
  );
};

export default CalculationCard;
