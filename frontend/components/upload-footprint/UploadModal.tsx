import { Dialog, Transition } from "@headlessui/react";
import { Fragment, useRef, useState } from "react";

interface IProps {
  isOpen: boolean;
  setIsOpen: (value: boolean) => void;
  onConfirm: () => void;
}

// Import React FilePond
import { FilePond, registerPlugin } from "react-filepond";
import { FilePondInitialFile } from "filepond";

// Import FilePond styles
import "filepond/dist/filepond.min.css";

// Import the Image EXIF Orientation and Image Preview plugins
import FilePondPluginImageExifOrientation from "filepond-plugin-image-exif-orientation";
import FilePondPluginImagePreview from "filepond-plugin-image-preview";
import FilePondPluginFileValidateType from "filepond-plugin-file-validate-type";
import FilePondPluginImageCrop from "filepond-plugin-image-crop";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css";
import RectangleButton from "../buttons/RectangleButton";

registerPlugin(
  FilePondPluginImageExifOrientation,
  FilePondPluginImagePreview,
  FilePondPluginFileValidateType,
  FilePondPluginImageCrop,
);

const UploadModal = ({ isOpen, setIsOpen, onConfirm }: IProps) => {
  const filepondRef = useRef<FilePond>(null);
  const [files, setFiles] = useState<any[]>([]);
  console.log(files);

  const closeModal = () => {
    setIsOpen(false);
  };

  return (
    <Transition appear show={isOpen} as={Fragment}>
      <Dialog as="div" className="relative z-10" onClose={closeModal}>
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
              <Dialog.Panel className="max-h-[40rem] w-full max-w-md transform overflow-hidden overflow-y-scroll rounded-2xl border border-navy-200/5 bg-navy-800 px-6 pt-6 pb-3 text-left align-middle shadow-xl transition-all">
                <Dialog.Title as="h3" className="text-lg font-medium leading-6 text-navy-200">
                  사진을 업로드하세요
                </Dialog.Title>
                <div className="mt-1 mb-4 text-sm text-navy-500">
                  사진의 정보를 분석해서 발자취를 쉽게 기록할 수 있도록 도와드려요.
                </div>
                <FilePond
                  files={files}
                  ref={filepondRef}
                  onupdatefiles={(files) => {
                    files.forEach((file) => {
                      file.file;
                      console.log(file.getMetadata());
                    });
                    setFiles(files);
                  }}
                  allowProcess={false}
                  instantUpload={false}
                  allowMultiple={true}
                  maxFiles={10}
                  server={{ url: "http://localhost:8000", process: "/api/v1/process" }}
                  name="files" /* sets the file input name, it's filepond by default */
                  labelIdle="이곳을 클릭하거나 사진을 드래그해서 업로드해보세요."
                  acceptedFileTypes={["image/jpeg", "image/png", "image/jpg", "image/heic"]}
                  imageCropAspectRatio="3:1"
                  maxParallelUploads={5}
                  itemInsertLocation="after"
                />
                <div className="flex justify-center">
                  <RectangleButton
                    text="분석하기"
                    onClick={() => {
                      filepondRef.current?.processFiles();
                    }}
                    isLoading={false}
                    disabled={files.length === 0}
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

export default UploadModal;
