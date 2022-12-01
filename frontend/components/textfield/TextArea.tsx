import { ChangeEventHandler } from "react";

interface IProps {
  value: string;
  onChange: (value: string) => void;
  placeholder: string;
}

const TextArea = ({ value, onChange, placeholder }: IProps) => {
  return (
    <textarea
      className="w-full rounded-lg border border-navy-800 bg-navy-800 p-3 text-sm outline-none transition-colors focus:border-navy-700"
      value={value}
      onChange={(e) => {
        onChange(e.target.value);
      }}
      placeholder={placeholder}
    />
  );
};

export default TextArea;
