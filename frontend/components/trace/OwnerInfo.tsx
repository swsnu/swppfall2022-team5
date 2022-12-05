import OwnerProfilePhoto from "./OwnerProfilePhoto";

const OwnerInfo = (profile: { username: string }) => {
    return (
        <div className="flex">
            <OwnerProfilePhoto imageUrl="https://images.unsplash.com/photo-1669072596436-15df4a8c083d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=987&q=80" />
            <div className="text-base p-3 text-center">
                {profile.username}
            </div>
        </div>
    )
}

export default OwnerInfo;