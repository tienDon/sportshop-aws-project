import React from "react";
import { Outlet } from "react-router";
import Header from "@/components/layout/header/Header";
import AnnouncementBanner from "@/components/layout/header/AnnouncementBanner";
import ChatBubble from "@/components/common/ChatBubble";
import Footer from "@/components/layout/Footer";

const MainLayout = () => {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <Header />
      <AnnouncementBanner />
      <main className="flex-1">
        <Outlet />
      </main>
      <ChatBubble />
      <Footer />
    </div>
  );
};

export default MainLayout;
