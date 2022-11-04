/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./pages/**/*.{js,ts,jsx,tsx}", "./components/**/*.{js,ts,jsx,tsx}"],
  theme: {
    container: {
      screens: {
        sm: "100%",
        md: "450px",
      },
    },
    extend: {
      colors: {
        navy: {
          50: "#f6f6f9",
          100: "#ebecf3",
          200: "#d3d5e4",
          300: "#acb0cd",
          400: "#7f86b1",
          500: "#5e6699",
          600: "#323657",
          700: "#23263c",
          800: "#181925",
          900: "#14151f",
        },
      },
    },
  },
  plugins: [],
};
