import React, { Component } from "react";
import { render } from "react-dom";
import axios from "axios";

class ImageRecognitionComponent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      processedImages: [],
      selectedFile: null,
    };
    this.uploadImage = this.uploadImage.bind(this);
    this.selectedImage = this.selectedImage.bind(this);
  }

  selectedImage(event) {
    this.setState({
      selectedFile: event.target.files[0],
    });
  }

  uploadImage() {
    var formData = new FormData();
    formData.append("image", this.state.selectedFile);
    axios
      .post("/api/upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then(() => {
        this.setState(() => {
          return {
            selectedFile: null,
          };
        });
        return axios.get("/api/findLatestFiveResults");
      })
      .then((images) => {
        console.log(images.data);
        this.setState({
          processedImages: images.data,
        });
      });
  }

  componentDidMount() {
    axios
      .get("/api/findLatestFiveResults")
      .then((response) => this.setState({ processedImages: response.data }));
  }

  render() {
    const { processedImages } = this.state;
    return (
      <div>
        <div>
        <h3>Demonstration of object recognition from image</h3>
          <div>
            <label>
              <input
                type="file"
                accept="image/*"
                onChange={this.selectedImage}
              /> &nbsp;&nbsp; <p>Please keep in mind, image upload has 3MB file size limitation</p>
            </label>
          </div>
          <div>
            <button onClick={this.uploadImage}>Upload</button>
          </div>
        </div>
        <div>
        <h3 className="center">List of Processed Images</h3>
          <ul>
            {processedImages &&
              processedImages.map((img, index) => (
                <li className="center" key={index}>
                  <img src={`data:${img.type};base64,${img.imageData}`} />
                  <br /> <br /> <br />
                </li>
              ))}
          </ul>
        </div>
      </div>
    );
  }
}

export default ImageRecognitionComponent;
