package top.liewyoung.network.response;

public record Usage (
        int prompt_tokens,
        int completion_tokens,
        int total_tokens,
        PromptTokensDetails prompt_tokens_details,
        int prompt_cache_hit_tokens,
        int prompt_cache_miss_tokens
){}
