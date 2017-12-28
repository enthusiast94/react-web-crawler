import { FETCH_SITEMAP_REQUEST, FETCH_SITEMAP_SUCCESS, FETCH_SITEMAP_FAILURE } from './actions';

export function sitemapReducer(
    state = {
        isFetching: false,
        error: null,
        sitemap: null
    }, action
) {
    switch (action.type) {
        case FETCH_SITEMAP_REQUEST:
            return Object.assign({}, state, {
                isFetching: true,
                error: null,
                sitemap: null
            });

        case FETCH_SITEMAP_SUCCESS:
            return Object.assign({}, state, {
                isFetching: false,
                error: null,
                sitemap: action.sitemap
            });

        case FETCH_SITEMAP_FAILURE:
            return Object.assign({}, state, {
                isFetching: false,
                error: action.error,
                sitemap: null
            });

        default:
            return state;
    }
}