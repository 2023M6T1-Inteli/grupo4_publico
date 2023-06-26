import { ConfigProvider } from "antd";
import React from "react";
import {
  Route,
  RouterProvider,
  createBrowserRouter,
  createRoutesFromElements,
} from "react-router-dom";
import CenterLayout from "./layouts/center-layout";
import HomePage from "./pages/home-page";
import MachinesPage from "./pages/machines-page";
import OderIdCalculationPage from "./pages/order-id-calculation-page";
import OrderIdPage from "./pages/order-id-page";
import OrdersPage from "./pages/orders-page";
import PageNotFound from "./pages/page-not-found";

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/">
      <Route element={<CenterLayout />}>
        <Route index element={<HomePage />} />
        <Route path="machines" element={<MachinesPage />} />
        <Route path="orders" element={<OrdersPage />}></Route>
      </Route>

      <Route path="/order/id/:id">
        <Route index element={<OrderIdPage />} />
        <Route path="calc/id/:calcId" element={<OderIdCalculationPage />} />
      </Route>

      <Route element={<CenterLayout />}>
        <Route path="/*" element={<PageNotFound />} />
      </Route>
    </Route>
  )
);

const App = () => {
  return (
    <>
      <ConfigProvider
        theme={{
          token: {
            colorPrimary: "#3F3F46",
          },
        }}
      >
        <RouterProvider router={router} />
      </ConfigProvider>
    </>
  );
};

export default App;
