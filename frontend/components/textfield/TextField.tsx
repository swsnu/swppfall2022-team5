import { ChangeEventHandler, HTMLInputTypeAttribute } from "react";

interface IProps {
  value: string;
  onChange: (value: string) => void;
  placeholder: string;
  type?: HTMLInputTypeAttribute;
}

const TextField = ({ value, onChange, placeholder, type }: IProps) => {
  return (
    <input
      type={type}
      className="w-full rounded-lg border border-navy-800 bg-[#0c0d13] p-3 text-sm outline-none transition-colors focus:border-navy-700"
      value={value}
      onChange={(e) => {
        onChange(e.target.value);
      }}
      placeholder={placeholder}
    />
  );
};

export default TextField;
