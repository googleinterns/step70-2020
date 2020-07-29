/** Button that triggers authorization/sign out flow */
const authorizeButton = document.getElementById('authorize-button');

/** Video Url textbox that the comment will be posted to */
const videoUrlElement = document.getElementById('video-url');

/** Textbox that allows user to input comments */
const postCommentElement = document.getElementById('comment-text');

/** Container that holds the success/error message */
const postResultContainer = document.getElementById('result-text');

/**
 * Overwrite the innerText of a DOM element identified by its Id.
 * @param {String} text
 * @param {String} containerDOM
 */
function updateDom(text, containerDOM) {
    while (containerDOM.firstChild) {
        containerDOM.removeChild(containerDOM.firstChild);
    }
    containerDOM.innerText = text;
}

/**
 * Modify the UI and link handle functions depending on the sign in status.
 * @exports
 * @param {Boolean} isSignedIn
 * @param {Function} handleAuthClick
 * @param {Function} handleSignoutClick
 * @param {Function} handleCommentClick
 */
function updateSigninStatus(isSignedIn, handleAuthClick, handleSignoutClick, handleCommentClick) {
    if (isSignedIn) {
        authorizeButton.value = 'LOG OUT'
        authorizeButton.onclick = handleSignoutClick;
        const commentButton = document.createElement('input');
        commentButton.type = 'button';
        commentButton.id = 'comment-button';
        commentButton.value = 'POST';
        commentButton.className = 'btn btn-primary';
        commentButton.onclick = handleCommentClick;
        const commentButtonContainer = document.getElementById('comment-button-container');
        while (commentButtonContainer.firstChild) {
            commentButtonContainer.removeChild(commentButtonContainer.firstChild);
        }
        commentButtonContainer.appendChild(commentButton);
    } else {
        updateDom('LOG IN', authorizeButton);
        authorizeButton.value = 'LOG IN';
        authorizeButton.onclick = handleAuthClick;
        const commentButton = document.getElementById('comment-button');
        commentButton && commentButton.remove();
    }
    updateDom('', postResultContainer);
}

/**
 * Error handler when the Google API client fails to load.
 * @exports
 * @param {Error} err
 */
function failedInitCallback(err) {
    console.error('init error', err);
    updateDom('Failed to initialize client', postResultContainer);
}

/**
 * Display 'successful' when the comment has been posted.
 * @exports
 * @param {*} data content is ignored
 */
function successfulApiCallback(data) {
    updateDom('Successfully posted your comment to YouTube', postResultContainer);
}

/**
 * Display the error message when the comment failed to be posted.
 * @exports
 * @param {Error} err
 */
function failedApiCallback(err) {
    console.error('execution error ', err);
    updateDom('An error occurred for posting this comment.', postResultContainer);
}

export { videoUrlElement, postCommentElement, failedInitCallback, successfulApiCallback, failedApiCallback, updateSigninStatus };
