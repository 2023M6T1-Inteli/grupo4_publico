import { Radio } from "antd";
import React from "react";

const MachinesUsed = (props) => {
  return (
    <>
      <Radio.Group>
        <Radio value={props.machineUsed} disabled>
          Máquina {props.machineUsed}
        </Radio>
      </Radio.Group>
    </>
  );
};

export default MachinesUsed;
