/** @type {import('next').NextConfig} */
const withPWA = require("next-pwa")({
  dest: "public",
});

const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  images: { domains: ["images.unsplash.com"] },
};

module.exports = withPWA(nextConfig);
