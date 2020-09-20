import React, {useState, useEffect, useCallback} from 'react';
import logo from './logo.svg';
import './App.css';
import axios from "axios";
import {useDropzone} from 'react-dropzone';

const UserProfiles = () =>{

  const [userProfiles, setUserProfiles] = useState([]);

  const fetchUserProfiles = () =>{
    axios.get("http://localhost:8080/api/v1/user-profile")
         .then(res => {
           console.log(res)
           setUserProfiles(res.data);
          });
  }

  useEffect(() => {
    fetchUserProfiles();
  }, []);

  

  return userProfiles.map((userProfile, index) => {
    var path = "http://localhost:8080/api/v1/user-profile/" + userProfile.userProfileId + "/image/download"; //ne znam zasto nije radilo kad sam stavio to sve skupa kao "http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload"
    return (
      <div key={index}>  
        {userProfile.userProfileId ? <img src={path}/> : null}      
        <br/>
        <br/>
        <h1>{userProfile.username}</h1>
        <p>{userProfile.userProfileId}</p>
        <Dropzone {...userProfile}/>
        <br/>
      </div>
    );
  });

}

function Dropzone({userProfileId}) {
  const onDrop = useCallback(acceptedFiles => {
    const file = acceptedFiles[0];
    console.log(file);

    const formData = new FormData();
    formData.append("file", file);

    var path = "http://localhost:8080/api/v1/user-profile/" + userProfileId + "/image/upload"; //ne znam zasto nije radilo kad sam stavio to sve skupa kao "http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload"

    axios.post(
      path, 
      formData, 
      {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      }

    ).then(() => {
      console.log("File uploaded successfully.");
    }).catch(err => {
      console.log(err);
    });

  }, []);
  const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the image here ...</p> :
          <p>Drag 'n' drop profile image, or click to select profile image</p>
      }
    </div>
  )
}

function App() {
  return (
    <div className="App">
      <UserProfiles/>
    </div>
  );
}

export default App;
