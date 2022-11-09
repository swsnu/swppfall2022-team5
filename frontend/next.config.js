/** @type {import('next').NextConfig} */
const withPWA = require("next-pwa")({
  dest: "public",
});

const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  images: { domains: ["images.unsplash.com", "footprinter-media.s3.ap-northeast-2.amazonaws.com"] },
};

module.exports = withPWA(nextConfig);
