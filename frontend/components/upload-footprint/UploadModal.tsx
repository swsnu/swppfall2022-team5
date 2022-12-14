import { Dialog, Transition } from "@headlessui/react";
import { useMutation } from "@tanstack/react-query";
import { Fragment, useRef, useState } from "react";
import { compress, compressAccurately } from "image-conversion";

interface IProps {
  isOpen: boolean;
  setIsOpen: (value: boolean) => void;
  onConfirm: () => void;
}

// Import React FilePond
import { FilePondFile } from "filepond";
import { FilePond, registerPlugin } from "react-filepond";

// Import FilePond styles
import "filepond/dist/filepond.min.css";

// Import the Image EXIF Orientation and Image Preview plugins
import FilePondPluginFileValidateSize from "filepond-plugin-file-validate-size";
import FilePondPluginFileValidateType from "filepond-plugin-file-validate-type";
import FilePondPluginImageCrop from "filepond-plugin-image-crop";
import FilePondPluginImageExifOrientation from "filepond-plugin-image-exif-orientation";
import FilePondPluginImagePreview from "filepond-plugin-image-preview";
import FilePondPluginImageTransform from "filepond-plugin-image-transform";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css";

import { useRouter } from "next/router";
import RectangleButton from "../buttons/RectangleButton";
import { fetchInitialFootprints } from "../../api";
import { useFootprintCreateStore } from "../../store/footprint";
import { useAuthStore } from "../../store/auth";

registerPlugin(
  FilePondPluginImageExifOrientation,
  FilePondPluginImagePreview,
  FilePondPluginFileValidateType,
  FilePondPluginImageCrop,
  FilePondPluginFileValidateSize,
  FilePondPluginImageTransform,
);

const UploadModal = ({ isOpen, setIsOpen, onConfirm }: IProps) => {
  const filepondRef = useRef<FilePond>(null);
  const [files, setFiles] = useState<any[]>([]);
  const [confirmDisabled, setConfirmDisabled] = useState(true);
  const router = useRouter();
  const mutation = useMutation((photoIds: string[]) => fetchInitialFootprints(photoIds));
  const setPendingFootprintRequest = useFootprintCreateStore((state) => state.setPendingFootprintRequests);
  const closeModal = () => {
    setIsOpen(false);
  };

  const startAnalysis = () => {
    const filePaths = files.map((file: FilePondFile) => file.serverId).filter((value) => !!value);
    mutation.mutateAsync(filePaths).then((data) => {
      setPendingFootprintRequest(data);
      closeModal();
      router.push("/footprints/create");
    });
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
                <div className="text-lg font-medium leading-6 text-navy-200">????????? ??????????????????</div>
                <div className="mt-1 mb-4 text-sm text-navy-400">
                  ????????? ????????? ???????????? ???????????? ?????? ????????? ??? ????????? ???????????????.
                </div>
                <FilePond
                  files={files}
                  ref={filepondRef}
                  onupdatefiles={(files) => {
                    setFiles(files);
                  }}
                  allowProcess={false}
                  allowMultiple={true}
                  onprocessfiles={() => {
                    setConfirmDisabled(false);
                  }}
                  maxFiles={10}
                  server={{
                    url: `${process.env.NEXT_PUBLIC_API_URL}`,
                    process: "/photos/process",
                    revert: "/photos/revert",
                    headers: {
                      Authorization: `Bearer ${useAuthStore((state) => state.userToken)}`,
                    },
                  }}
                  name="files" /* sets the file input name, it's filepond by default */
                  labelIdle="????????? ??????????????? ????????? ??????????????? ?????????????????????."
                  acceptedFileTypes={["image/jpeg", "image/png", "image/jpg", "image/heic", "image/heif"]}
                  // imageCropAspectRatio="3:1"
                  maxParallelUploads={5}
                  itemInsertLocation="after"
                  maxFileSize="10MB"
                  imageTransformOutputMimeType="image/jpeg"
                  imageTransformOutputQuality={80}
                  imageTransformOutputStripImageHead={false}
                />
                <div className="flex justify-center gap-4">
                  <RectangleButton
                    text="????????????"
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

export default UploadModal;
