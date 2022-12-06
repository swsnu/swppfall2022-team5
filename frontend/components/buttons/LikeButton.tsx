import { IconHeart, IconThumbUp, IconThumbUpOff, TablerIcon } from "@tabler/icons";

interface IProps {
  isLiked: boolean;
  onClick: () => void;
  likesCount: number;
}

const LikeButton = ({ isLiked, onClick, likesCount }: IProps) => {
  return (
    <button
      className="flex items-center gap-1 text-sm text-navy-200 transition-colors hover:text-navy-100"
      onClick={onClick}
    >
      {isLiked ? <IconHeart fill="#fff" /> : <IconHeart />}
      <span>{likesCount}</span>
    </button>
  );
};

export default LikeButton;
