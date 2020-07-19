/** Button that triggers authorization/sign out flow */
const authorizeButton = document.getElementById('authorize-button');

/** Hardcoded test video Id that the comment will be posted to */
const videoId = 'gjKpAAezWQA';

/** Textbox that allows user to input comments */
const commentElement = document.getElementById('comment-text');

/** Container that holds the success/error message */
const resultContainer = document.getElementById('result-text');

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

/**
 * Error handler when the Google API client fails to load.
 * @exports
 * @param {Error} err 
 */
function failedInitCallback(err) {
    console.error('init error', err)
    updateDom('Failed to initialize client', resultContainer)
}

/**
 * Display 'successful' when the comment has been posted.
 * @exports
 * @param {*} data 
 */
function successfulApiCallback(data) {
    updateDom('successful', resultContainer);
}

/**
 * Display the error message when the comment failed to be posted.
 * @exports
 * @param {Error} err
 */
function failedApiCallback(err) {
    console.error('execution error ', err);
    updateDom(err, resultContainer);
}

export { videoId, commentElement, failedInitCallback, successfulApiCallback, failedApiCallback, updateSigninStatus };