function updateDom(text, containerDOM) {
    while (containerDOM.firstChild) {
        containerDOM.removeChild(containerDOM.firstChild);
    }
    containerDOM.innerText = text;
}

const authorizeButton = document.getElementById('authorize-button');

const videoId = 'gjKpAAezWQA';
const commentElement = document.getElementById('comment-text');
const resultContainer = document.getElementById('result-text');

function updateSigninStatus(isSignedIn, handleAuthClick, handleSignoutClick, handleCommentClick) {
    if (isSignedIn) {
        updateDom('Sign out', authorizeButton)
        authorizeButton.onclick = handleSignoutClick;
        const commentButton = document.createElement('button');
        document.getElementById('comment-button-container').appendChild(commentButton);
        commentButton.id = 'comment-button';
        commentButton.innerText = 'Post a comment';
        commentButton.onclick = handleCommentClick;
    } else {
        updateDom('Authorize', authorizeButton);
        authorizeButton.onclick = handleAuthClick;
        const commentButton = document.getElementById('comment-button');
        commentButton && commentButton.remove();
    }
}

function failedInit(err) {
    console.error('init error', err)
    updateDom('Failed to initialize client', resultContainer)
}

function successfulApiCallback(data) {
    updateDom('successful', resultContainer);
}

function failedApiCallback(err) {
    console.error('execution error ', err);
    updateDom(err, resultContainer);
}

export { videoId, commentElement, resultContainer, failedInit, successfulApiCallback, failedApiCallback, updateSigninStatus };