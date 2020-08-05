import positive from '../positive.js';

const FAKE_ERROR = new Error('error message');

const mockGetVideoResponse = {
  items: [{
    id: 'sampleID',
    statistics: { likeCount: 1},
    snippet: {
        title: 'Example Title',
        thumbnails: { medium: { url: 'sampleURL'}}
    }
  }
  ]
};

const mockFetchResponse = new window.Response('[{"sentimentScore":0.5,"videoId":"sampleId"}]');

QUnit.module("list videos", {
  beforeEach: function() {
    const getVideoFromYoutubeApi = sinon.fake.returns(Promise.resolve(mockGetVideoResponse));
    sinon.replace(positive, 'getVideoFromYoutubeApi', getVideoFromYoutubeApi);
  },
  afterEach: function() {
    sinon.restore();
  },
});

QUnit.test('loadPositive - normal use', function(assert) {
  const done = assert.async();
  sinon.stub(window, 'fetch').returns(Promise.resolve(mockFetchResponse));
  positive.loadPositive().then(() => {
    assert.dom('.card').exists({ count: 1 });
    done();
  });
});

QUnit.test('loadPopular - database error', function(assert) {
  const done = assert.async();
  sinon.stub(window, 'fetch').returns(Promise.reject(FAKE_ERROR));
  positive.loadPositive().then(() => {
    assert.dom('#positive-list-container').hasText('An error occurred getting videos');
    done();
  });
});
