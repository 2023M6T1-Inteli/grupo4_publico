import { Space } from "antd";
import React, { useEffect, useState } from "react";
import { ReactComponent as Logo } from "../assets/logo.svg";
import CalculationCard from "../components/calculation-card";
import axios from "axios";
import CalculationNotFound from "./calculation-not-found";

const RightCol = (props) => {
  const [calcs, setCalcs] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/calc").then((res) => {
      setCalcs(res.data);
    });
  }, []);

  return (
    <>
      <Logo
        className="main-logo"
        style={{ marginTop: "50px", marginBottom: "50px" }}
      />
      {/* <CalculationNotFound /> */}
      <Space
        direction="vertical"
        size="small"
        style={{
          display: "flex",
        }}
      >
        {calcs.map((calc) => {
          return (
            <>
              {calc.orderSetId === props.orderSetId ? (
                <CalculationCard calcName={calc.name} calcId={calc.id} />
              ) : (
                ""
              )}
            </>
          );
        })}
      </Space>
    </>
  );
};

export default RightCol;
