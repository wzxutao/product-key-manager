import React from 'react'


const ManagementPage = () => {
    return (
        <div>
            <button onClick={() => {throw new Error("")}}>b</button>
        </div>
    )
}

export default ManagementPage;