import positive from '../positive.js';

const FAKE_ERROR = new Error('error message');
const mockFetchResponse = new window.Response('[{"sentimentScore":0.5,"videoId":"sampleId"}]');

QUnit.test('loadPositive - API error', function(assert) {
  const done = assert.async();
  document.getElementById('positive-list-container').innerHTML = '';
  sinon.stub(window, 'fetch').returns(Promise.resolve(mockFetchResponse));
  const getVideoFromYoutubeApi = sinon.fake.returns(Promise.reject(FAKE_ERROR));
  sinon.replace(positive, 'getVideoFromYoutubeApi', getVideoFromYoutubeApi);

  positive.loadPositive().then(() => {
    assert.dom('#positive-list-container').hasText('An error occurred with YouTube');
    sinon.restore();
    done();
  });
});
