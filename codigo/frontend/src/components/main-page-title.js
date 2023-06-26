import { Typography } from "antd";
import React from "react";
import "../styles/components-style.css";

const { Title, Text } = Typography;

const MainPageTitle = (props) => {
  return (
    <>
      <div className="margin-top-high">
        <Text>Você está em</Text>
        <Title level={2} style={{ marginTop: "0" }}>
          {props.title}
        </Title>
      </div>
    </>
  );
};

export default MainPageTitle;
