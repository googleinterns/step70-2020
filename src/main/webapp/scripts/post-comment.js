/**
 * Post a video comment by making a POST request to YouTube Data API.
 * @exports
 * @param {String} videoId 
 * @param {String} commentText 
 * @returns {String} empty string if successful post
 * @throws {Error} with error message if failed post
 */
function postVideoComment(videoId, commentText) {
    return new Promise((resolve, reject) => {
        gapi.client.request(buildRequestBody(videoId, commentText))
            .then(res => res.json)
            .then(data => resolve(data))
            .catch(err => reject(new Error(err.result.error.message)));
    });
}

/**
 * Build a request body expected by the API.
 * @private
 * @param {String} videoId 
 * @param {String} commentContent 
 * @returns {Object} request body for a POST comment request by video ID
 */
function buildRequestBody(videoId, commentContent) {
    return {
        'path': '/youtube/v3/commentThreads?part=snippet',
        'method': 'POST',
        'body': {
            'snippet': {
                'videoId': videoId,
                'topLevelComment': {
                    'snippet': {
                        'textOriginal': commentContent
                    },
                },
            },
        },
    };
}

export { postVideoComment };