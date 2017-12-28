export const FETCH_SITEMAP_REQUEST = "fetch_sitemap_request";
export const FETCH_SITEMAP_SUCCESS = "fetch_sitemap_success";
export const FETCH_SITEMAP_FAILURE = "fetch_sitemap_failure";

export function fetchSitemapRequest() {
    return {
        type: FETCH_SITEMAP_REQUEST
    };
}

export function fetchSitemapSuccess(sitemap) {
    return {
        type: FETCH_SITEMAP_SUCCESS,
        sitemap: sitemap
    };
}

export function fetchSitemapFailure(error) {
    return {
        type: FETCH_SITEMAP_FAILURE,
        error: error
    };
}