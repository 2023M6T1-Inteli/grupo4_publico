import { Typography } from "antd";
import React from "react";
import "../styles/components-style.css";

const { Title, Text } = Typography;

const CalculationNotFound = () => {
  return (
    <>
      <Title level={3}>Nenhum cálculo encontrado</Title>
      <Text>
        Um histórico será exibido aqui quando você solicitar um cálculo.
      </Text>
    </>
  );
};

export default CalculationNotFound;
