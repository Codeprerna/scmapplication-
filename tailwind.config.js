/** @type {import('tailwindcss').Config} */
export default {
  content: ["./src/main/resources/**/*.{html,js}"],
  theme: {
    extend: {},
  },
  plugins: [
    require('tailwind-scrollbar')({ nocompatible: true }),
  ],
  darkMode: "selector",
};

