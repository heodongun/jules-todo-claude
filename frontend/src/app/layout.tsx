import type { Metadata } from 'next';
import './globals.css';

export const metadata: Metadata = {
  title: 'Board Application',
  description: 'A CRUD board application built with Next.js and Ktor',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
