import { Dialog, Transition } from "@headlessui/react";
import { Fragment } from "react";

interface IProps {
  isOpen: boolean;
  setIsOpen: (value: boolean) => void;
  onConfirm: () => void;
}

const PlaceSelectModal = ({ isOpen, setIsOpen, onConfirm }: IProps) => {
  const closeModal = () => {
    setIsOpen(false);
  };
  return (
    <Transition appear show={isOpen} as={Fragment}>
      <Dialog as="div" className="relative z-50" onClose={closeModal}>
        <Transition.Child
          as={Fragment}
          enter="ease-out duration-300"
          enterFrom="opacity-0"
          enterTo="opacity-100"
          leave="ease-in duration-200"
          leaveFrom="opacity-100"
          leaveTo="opacity-0"
        >
          <div className="fixed inset-0 bg-black bg-opacity-25" />
        </Transition.Child>

        <div className="fixed inset-0 overflow-y-auto">
          <div className="flex min-h-full items-center justify-center p-4 text-center">
            <Transition.Child
              as={Fragment}
              enter="ease-out duration-300"
              enterFrom="opacity-0 scale-95"
              enterTo="opacity-100 scale-100"
              leave="ease-in duration-200"
              leaveFrom="opacity-100 scale-100"
              leaveTo="opacity-0 scale-95"
            >
              <Dialog.Panel className="max-h-[40rem] w-full max-w-lg transform overflow-hidden overflow-y-scroll rounded-2xl border border-navy-200/5 bg-navy-800 px-6 py-5 text-left align-middle shadow-xl transition-all">
                <div className="text-lg font-medium leading-6 text-navy-200">사진을 업로드하세요</div>
                <div className="mt-1 mb-4 text-sm text-navy-400">
                  사진의 정보를 분석해서 발자취를 쉽게 기록할 수 있도록 도와드려요.
                </div>
                <div className="flex justify-center gap-4">
                  <RectangleButton
                    text="분석하기"
                    onClick={() => {
                      startAnalysis();
                    }}
                    isLoading={mutation.isLoading}
                    disabled={files.length === 0 || confirmDisabled}
                    className="grow"
                  />
                </div>
              </Dialog.Panel>
            </Transition.Child>
          </div>
        </div>
      </Dialog>
    </Transition>
  );
};

export default PlaceSelectModal;
