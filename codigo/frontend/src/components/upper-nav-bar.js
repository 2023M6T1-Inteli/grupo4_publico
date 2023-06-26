import { FolderOutlined, SettingOutlined } from "@ant-design/icons";
import { Button, Space } from "antd";
import React from "react";
import { Link, useLocation } from "react-router-dom";
import { ReactComponent as Logo } from "../assets/logo.svg";
import { MACHINE_TEXT, ORDERS_TEXT } from "../constants/upper-nav-bar";
import "../styles/components-style.css";

const UpperNavBar = () => {
  const location = useLocation();

  return (
    <>
      <div className="upper-nav-bar">
        <div className="center-content">
          <Logo className="main-logo" />
        </div>

        <Space />

        <div>
          <Link to="/machines">
            <Button type="text">
              <SettingOutlined className="icon-style" />
              {location.pathname === "/machines" ? (
                <b>{MACHINE_TEXT}</b>
              ) : (
                MACHINE_TEXT
              )}
            </Button>
          </Link>
          <Link to="/orders">
            <Button type="text">
              <FolderOutlined className="icon-style" />
              {location.pathname === "/orders" ? (
                <b>{ORDERS_TEXT}</b>
              ) : (
                ORDERS_TEXT
              )}
            </Button>
          </Link>
        </div>
      </div>
    </>
  );
};

export default UpperNavBar;
