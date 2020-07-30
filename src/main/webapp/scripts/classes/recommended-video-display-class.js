class RecommendedVideoDisplay{
  #CARD = { element: 'div', className: 'card'};
  #CARDBODY = { element: 'div', className: 'card-body p-3'};
  #THUMBNAIL = { element: 'img', className: 'card-img-top'};
  #TITLE = { element: 'h5', className: 'card-title'};
  #TEXT = { element: 'p', className: 'card-text'};

  constructor(containerId) {
    this.dom = document.getElementById(containerId);
    this.dom.className = 'row mt-4';
  }

  addVideo(video) {
    console.log(video);
    const videoCard = this.createVideoCard(video);
    const newElement = document.createElement('div');
    newElement.className = "col-lg-3 col-md-4 col-sm-6 col-xs-12";
    newElement.appendChild(videoCard);
    this.dom.appendChild(newElement);
  }

  createVideoCard(video) {
    const card = this.newElement(this.#CARD);
    const cardBody = this.newElement(this.#CARDBODY);
    const thumbnail = this.newElement(this.#THUMBNAIL);
    const title = this.newElement(this.#TITLE);
    const text = this.newElement(this.#TEXT);
    thumbnail.src = video.thumbnail;
    title.innerText = video.title;
    title.title =  video.title;
    if(video.sentiment === null) {
      text.innerText = video.likeCount + ' likes';
    } else {
      text.innerText = video.sentiment;
    }
    cardBody.appendChild(title);
    cardBody.appendChild(text);
    card.appendChild(thumbnail);
    card.appendChild(cardBody);
    return card;
  }

  newElement(type) {
    const element = document.createElement(type.element);
    element.className = type.className;
    return element
  }
}

 export { RecommendedVideoDisplay }
