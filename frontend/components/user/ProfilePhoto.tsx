import { FilePond, registerPlugin } from "react-filepond";
import { useState } from "react";

// Import FilePond styles
import "filepond/dist/filepond.min.css";

// Import the Image EXIF Orientation and Image Preview plugins
import FilePondPluginFileValidateSize from "filepond-plugin-file-validate-size";
import FilePondPluginFileValidateType from "filepond-plugin-file-validate-type";
import FilePondPluginImageCrop from "filepond-plugin-image-crop";
import FilePondPluginImageExifOrientation from "filepond-plugin-image-exif-orientation";
import FilePondPluginImagePreview from "filepond-plugin-image-preview";
import FilePondPluginImageTransform from "filepond-plugin-image-transform";
import FilePondPluginImageResize from "filepond-plugin-image-resize";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css";
import { useAuthStore } from "../../store/auth";
import { useQuery } from "@tanstack/react-query";
import { whoAmI } from "../../api";
import { UserResponseType } from "../../dto/user";
import { FilePondFile, FilePondInitialFile } from "filepond";

registerPlugin(
  FilePondPluginImageExifOrientation,
  FilePondPluginImagePreview,
  FilePondPluginFileValidateType,
  FilePondPluginImageCrop,
  FilePondPluginFileValidateSize,
  FilePondPluginImageTransform,
  FilePondPluginImageResize,
);

interface IProps {
  userProfile: UserResponseType;
  isEditing: boolean;
  files: any[];
  setFiles: (files: any[]) => void;
}

const ProfilePhoto = (props: IProps) => {
  const token = useAuthStore((state) => state.userToken);
  const myName = useQuery(["whoAmI"], whoAmI);

  if (!myName.isSuccess) {
    return <div>로딩중</div>;
  }

  return (
    <div className="relative h-28 w-28">
      {props.isEditing ? (
        <FilePond
          files={props.files}
          onupdatefiles={props.setFiles}
          allowMultiple={false}
          maxFiles={1}
          server={{
            url: `${process.env.NEXT_PUBLIC_API_URL}`,
            process: "/photos/user/process",
            revert: "/photos/user/revert",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }}
          name="files"
          labelIdle="업로드"
          acceptedFileTypes={["image/jpeg", "image/png", "image/jpg", "image/heic", "image/heif"]}
          stylePanelLayout={"compact circle"}
          styleLoadIndicatorPosition={"center bottom"}
          styleProgressIndicatorPosition={"center bottom"}
          styleButtonRemoveItemPosition={"center bottom"}
          styleButtonProcessItemPosition={"center bottom"}
          imagePreviewHeight={170}
          imageCropAspectRatio="1:1"
          imageResizeTargetWidth={200}
          imageResizeTargetHeight={200}
          imageTransformOutputQuality={80}
          imageTransformOutputStripImageHead={false}
          imageTransformOutputMimeType="image/jpeg"
        />
      ) : (
        <div className="relative h-28 w-28 flex-shrink-0 overflow-hidden rounded-full border border-gray-700">
          <img src={props.userProfile.imageUrl} alt={""} className="object-cover" />
        </div>
      )}
    </div>
  );
};

export default ProfilePhoto;
