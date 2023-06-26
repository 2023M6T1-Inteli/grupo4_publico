import { PlusOutlined } from "@ant-design/icons";
import { Button, message } from "antd";
import React, { useState, useEffect } from "react";
import MachineCard from "../components/machine-card";
import MachineModal from "../components/machine-modal";
import UpperNavBar from "../components/upper-nav-bar";
import "../styles/components-style.css";
import axios from "axios";

const MachinesPage = () => {
  const [machines, setMachines] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/machine").then((res) => {
      setMachines(res.data);
    });
  }, []);

  const [modalOpen, setModalOpen] = useState(false);

  const handleOk = () => {
    message.success("Máquina adicionada");
    setModalOpen(false);
  };

  const handleCancel = () => {
    message.warning("Máquina não adicionada");
    setModalOpen(false);
  };

  return (
    <>
      <UpperNavBar />

      <Button
        block
        type="primary"
        className="button-default"
        onClick={() => {
          setModalOpen(true);
        }}
      >
        <PlusOutlined className="icon-style" />
        Adicionar máquina
      </Button>

      <MachineModal
        modalOpen={modalOpen}
        handleCancel={handleCancel}
        handleOk={handleOk}
        machines={machines}
        setMachines={setMachines}
      />

      {machines.map((machine) => {
        return (
          <MachineCard
            key={machine.id}
            id={machine.id}
            machineName={machine.name}
            machineMaxwidth={machine.maxWidth}
            machineKnives={machine.knives}
            machines={machines}
            setMachines={setMachines}
          />
        );
      })}
    </>
  );
};

export default MachinesPage;
