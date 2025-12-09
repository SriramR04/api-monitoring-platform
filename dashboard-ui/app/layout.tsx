import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "API Monitoring Dashboard",
  description: "Monitor and analyze API performance",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}