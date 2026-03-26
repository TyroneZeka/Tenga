import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useEffect } from 'react'
import { Link } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'
import { useProfile, useUpdateProfile, useUploadAvatar } from '@/features/user'
import { useAuthStore } from '@/stores/authStore'

const schema = z.object({
  displayName: z.string().min(2, 'Name must be at least 2 characters'),
  bio: z.string().max(300, 'Bio must be 300 characters or less').optional(),
})

type FormValues = z.infer<typeof schema>

export default function EditProfilePage() {
  const user = useAuthStore((s) => s.user)!
  const { data: profile } = useProfile(user.id)
  const { mutate: updateProfile, isPending } = useUpdateProfile(user.id)
  const { mutate: uploadAvatar } = useUploadAvatar(user.id)

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<FormValues>({ resolver: zodResolver(schema) })

  useEffect(() => {
    if (profile) {
      reset({ displayName: profile.displayName, bio: profile.bio ?? '' })
    }
  }, [profile, reset])

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="mx-auto max-w-xl px-4 py-6">
        <Link
          to={`/users/${user.id}`}
          className="mb-4 flex items-center gap-1 text-sm text-gray-500 hover:text-gray-800"
        >
          <ArrowLeft className="h-4 w-4" /> Back
        </Link>
        <h1 className="mb-5 text-xl font-semibold text-gray-900">Edit profile</h1>

        <form
          onSubmit={handleSubmit((data) => updateProfile(data))}
          className="space-y-4 rounded-xl bg-white p-5 shadow-sm"
        >
          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">Avatar</label>
            <input
              type="file"
              accept="image/*"
              onChange={(e) => {
                const file = e.target.files?.[0]
                if (file) uploadAvatar(file)
              }}
              className="w-full text-sm text-gray-600 file:mr-3 file:rounded-lg file:border-0 file:bg-primary-50 file:px-3 file:py-1.5 file:text-sm file:text-primary-700 hover:file:bg-primary-100"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">Display name</label>
            <input
              {...register('displayName')}
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500"
            />
            {errors.displayName && (
              <p className="mt-1 text-xs text-red-600">{errors.displayName.message}</p>
            )}
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">Bio</label>
            <textarea
              {...register('bio')}
              rows={3}
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500"
            />
            {errors.bio && <p className="mt-1 text-xs text-red-600">{errors.bio.message}</p>}
          </div>

          <button
            type="submit"
            disabled={isPending}
            className="w-full rounded-lg bg-primary-500 py-2.5 text-sm font-medium text-white hover:bg-primary-600 disabled:opacity-50"
          >
            {isPending ? 'Saving…' : 'Save changes'}
          </button>
        </form>
      </div>
    </div>
  )
}
