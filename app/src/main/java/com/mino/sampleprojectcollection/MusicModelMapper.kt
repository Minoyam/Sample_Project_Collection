package com.mino.sampleprojectcollection

import com.mino.sampleprojectcollection.service.MusicDto
import com.mino.sampleprojectcollection.service.MusicEntity

fun MusicEntity.mapper(id: Long): MusicModel =
    MusicModel(
        id = id,
        streamUrl = streamUrl,
        coverUrl = coverUrl,
        track = track,
        artist = artist
    )

fun MusicDto.mapper(): PlayerModel =
    PlayerModel(
        playMusicModel = musics.mapIndexed { index, musicEntity ->
            musicEntity.mapper(index.toLong())
        }
    )