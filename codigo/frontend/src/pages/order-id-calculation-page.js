import { Button } from "antd";
import { ArrowLeftOutlined } from "@ant-design/icons";
import React, { useEffect, useState } from "react";
import "../styles/components-style.css";
import "../styles/pages-styles.css";
import { useParams } from "react-router-dom";
import axios from "axios";
import { Link } from "react-router-dom";
import MainPageTitle from "../components/main-page-title";
import GraphCoil from "../components/graphic-coil";

const OderIdCalculationPage = () => {
  const { id } = useParams();
  const { calcId } = useParams();

  const [calc, setCalc] = useState({});
  const [requested, setRequested] = useState(false);

  useEffect(() => {
    axios.get(`http://localhost:8080/calc/${calcId}`).then((res) => {
      setCalc(res.data);
      setRequested(true);
    });
  });

  return (
    <>
      <div style={{ margin: "50px" }}>
        <Link to={`/order/id/${id}`}>
          <Button type="text">
            <ArrowLeftOutlined style={{ marginRight: "5px" }} />
            <b>Voltar</b>
          </Button>
        </Link>
        <MainPageTitle title={calc.name} />
        <GraphCoil calc={calc} requested={requested} />
      </div>
    </>
  );
};

export default OderIdCalculationPage;
