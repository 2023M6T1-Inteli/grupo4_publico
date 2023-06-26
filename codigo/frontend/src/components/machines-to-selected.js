import { Radio, Typography } from "antd";
import React, { useEffect, useState } from "react";
import axios from "axios";

const { Title } = Typography;

const MachinesToSelected = (props) => {
  const [machines, setMachines] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/machine").then((res) => {
      setMachines(res.data);
    });
  }, []);

  const [value, setValue] = useState();
  const onChange = (e) => {
    setValue(e.target.value);
    props.setMachineValue(e.target.value);
  };

  return (
    <>
      <div style={{ margin: "10px" }}>
        <Title level={5} style={{ marginTop: "0" }}>
          Máquinas
        </Title>
        <Radio.Group onChange={onChange} value={value}>
          {machines.map((machine) => {
            return (
              <Radio key={machine.id} value={machine.id}>
                {machine.name}
              </Radio>
            );
          })}
        </Radio.Group>
      </div>
    </>
  );
};

export default MachinesToSelected;
