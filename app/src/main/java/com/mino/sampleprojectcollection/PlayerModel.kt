package com.mino.sampleprojectcollection

data class PlayerModel(
    private val playMusicModel: List<MusicModel> = emptyList(),
    var currentPosition: Int = -1,
    var isWatchingPlayListView: Boolean = true
) {
    fun getAdapterModels(): List<MusicModel> {
        return playMusicModel.mapIndexed { index, musicModel ->
            val newItem = musicModel.copy(
                isPlaying = index == currentPosition
            )
            newItem
        }
    }

    fun updateCurrentPosition(musicModel: MusicModel) {
        currentPosition = playMusicModel.indexOf(musicModel)
    }

    fun nextMusic(): MusicModel? {
        if (playMusicModel.isEmpty()) return null

        currentPosition = if (currentPosition + 1 == playMusicModel.size) 0 else currentPosition + 1
        return playMusicModel[currentPosition]
    }

    fun prevMusic(): MusicModel? {
        if (playMusicModel.isEmpty()) return null

        currentPosition =
            if (currentPosition - 1 < 0) playMusicModel.lastIndex else currentPosition - 1

        return playMusicModel[currentPosition]
    }

    fun currentMusicModel(): MusicModel? {
        if (playMusicModel.isEmpty()) return null

        return playMusicModel[currentPosition]
    }
}