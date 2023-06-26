import { LoginOutlined } from "@ant-design/icons";
import { Button } from "antd";
import Paragraph from "antd/es/typography/Paragraph";
import Title from "antd/es/typography/Title";
import React from "react";
import { Link } from "react-router-dom";
import { ReactComponent as Logo } from "../assets/logo.svg";
import {
  BUTTON_TEXT,
  PARAGRAPH_TEXT,
  TITLE_TEXT,
} from "../constants/page-not-found";
import "../styles/components-style.css";
import "../styles/pages-styles.css";

const PageNotFound = () => {
  return (
    <>
      <div className="center-content">
        <Logo className="main-logo" />
      </div>
      <Title
        className="font title"
        style={{ marginTop: "20px", marginBottom: "20px" }}
      >
        {TITLE_TEXT}
      </Title>
      <Paragraph
        className="font"
        style={{ marginTop: "15px", marginBottom: "15px" }}
      >
        {PARAGRAPH_TEXT}
      </Paragraph>
      <Link to="/">
        <Button
          type="primary"
          className="button-home"
          block
          style={{ marginTop: "70px" }}
        >
          {BUTTON_TEXT}
          <LoginOutlined style={{ marginLeft: "15px" }} />
        </Button>
      </Link>
    </>
  );
};

export default PageNotFound;
